package com.triangleleft.flashcards;

import com.annimon.stream.function.Function;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
public class Calls {

    public static <T> Call<T> empty() {
        return new Call<T>() {
            @Override
            public void enqueue(Action<T> onData, Action<Throwable> onError) {

            }

            @Override
            public void cancel() {

            }
        };
    }

    public static <T, M> Call<T> map(Call<M> originalCall, Function<M, T> mapper) {
        return new CallDataMap<T, M>(originalCall) {
            @Override
            public T map(M data) {
                return mapper.apply(data);
            }
        };
    }

    public static <T> Call<T> just(T data) {
        return new Call<T>() {
            @Override
            public void enqueue(Action<T> onData, Action<Throwable> onError) {
                onData.call(data);
            }

            @Override
            public void cancel() {

            }
        };
    }

    public static <T> Call<T> error(Throwable throwable) {
        return new Call<T>() {
            @Override
            public void enqueue(Action<T> onData, Action<Throwable> onError) {
                onError.call(throwable);
            }

            @Override
            public void cancel() {

            }
        };
    }


    private static abstract class CallDataMap<T, M> implements Call<T> {

        private final Call<M> originalCall;

        public CallDataMap(Call<M> originalCall) {
            this.originalCall = originalCall;
        }

        public abstract T map(M data);

        @Override
        public final void enqueue(Action<T> onData, Action<Throwable> onError) {
            originalCall.enqueue(data -> onData.call(map(data)), onError::call);
        }

        @Override
        public final void cancel() {
            originalCall.cancel();
        }
    }

}
