package com.triangleleft.flashcards.service.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;

/**
 * Custom adapter factory similar to {@link retrofit2.adapter.rxjava.RxJavaCallAdapterFactory}.
 * Supports Observable<T> return types only.
 * Exception mapping is done in {@link OperatorMapResponseToBodyOrError}
 */
public class FlashcardsCallAdapterFactory extends CallAdapter.Factory {

    private final Scheduler scheduler;

    public FlashcardsCallAdapterFactory(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        if (rawType != Observable.class) {
            return null;
        }

        return getCallAdapter(returnType, scheduler);
    }

    private CallAdapter<Observable<?>> getCallAdapter(Type returnType, Scheduler scheduler) {
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        return new SimpleCallAdapter(observableType, scheduler);
    }

    static final class SimpleCallAdapter implements CallAdapter<Observable<?>> {
        private final Type responseType;
        private final Scheduler scheduler;

        SimpleCallAdapter(Type responseType, Scheduler scheduler) {
            this.responseType = responseType;
            this.scheduler = scheduler;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> Observable<R> adapt(Call<R> call) {
            Observable<R> observable = Observable.create(new CallOnSubscribe<>(call)) //
                    .lift(OperatorMapResponseToBodyOrError.<R>instance());
            if (scheduler != null) {
                return observable.subscribeOn(scheduler);
            }
            return observable;
        }
    }

    /**
     * Copied without changes form {@link retrofit2.adapter.rxjava.RxJavaCallAdapterFactory}
     *
     * @param <T> call type
     */
    static final class CallOnSubscribe<T> implements Observable.OnSubscribe<Response<T>> {
        private final Call<T> originalCall;

        CallOnSubscribe(Call<T> originalCall) {
            this.originalCall = originalCall;
        }

        @Override
        public void call(final Subscriber<? super Response<T>> subscriber) {
            // Since Call is a one-shot type, clone it for each new subscriber.
            Call<T> call = originalCall.clone();

            // Wrap the call in a helper which handles both unsubscription and backpressure.
            RequestArbiter<T> requestArbiter = new RequestArbiter<>(call, subscriber);
            subscriber.add(requestArbiter);
            subscriber.setProducer(requestArbiter);
        }
    }

    /**
     * Copied without changes form {@link retrofit2.adapter.rxjava.RxJavaCallAdapterFactory}
     *
     * @param <T> call type
     */
    static final class RequestArbiter<T> extends AtomicBoolean implements Subscription, Producer {
        private final Call<T> call;
        private final Subscriber<? super Response<T>> subscriber;

        RequestArbiter(Call<T> call, Subscriber<? super Response<T>> subscriber) {
            this.call = call;
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("n < 0: " + n);
            }
            if (n == 0) {
                return; // Nothing to do when requesting 0.
            }
            if (!compareAndSet(false, true)) {
                return; // Request was already triggered.
            }

            try {
                Response<T> response = call.execute();
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(response);
                }
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(t);
                }
                return;
            }

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }

        @Override
        public void unsubscribe() {
            call.cancel();
        }

        @Override
        public boolean isUnsubscribed() {
            return call.isCanceled();
        }
    }
}
