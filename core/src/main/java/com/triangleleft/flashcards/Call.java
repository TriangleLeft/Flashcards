package com.triangleleft.flashcards;

import com.annimon.stream.function.Function;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
public abstract class Call<T> {

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

    public static <T> Call<T> just(T data) {
        return new Call<T>() {
            @Override
            public void enqueue(Action<T> onData, Action<Throwable> onError) {

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

            }

            @Override
            public void cancel() {

            }
        };
    }

    public abstract void enqueue(Action<T> onData, Action<Throwable> onError);

    public abstract void cancel();

    public void enqueue(Observer<T> observer) {

    }

    public <M> Call<M> map(Function<T, M> mapper) {
        Call<T> self = this;
        return new Call<M>() {
            @Override
            public void enqueue(Action<M> onData, Action<Throwable> onError) {

            }

            @Override
            public void cancel() {

            }
        };
    }
}