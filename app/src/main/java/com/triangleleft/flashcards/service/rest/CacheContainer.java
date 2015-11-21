package com.triangleleft.flashcards.service.rest;

import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * Container for cached data.
 *
 * TODO: consider using guava CacheBuilder, or implement data expiration via timer.
 * @param <T>
 */
public class CacheContainer<T> {

    private T data;
    private long putTime;
    private final long expireDuration;

    public CacheContainer(long expireDuration, TimeUnit timeUnit) {
        this.expireDuration = TimeUnit.MILLISECONDS.convert(expireDuration, timeUnit);
    }

    public void put(@Nullable T data) {
        this.data = data;
        this.putTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }

    @Nullable
    public T get() {
        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - putTime;
        if (elapsedTime > expireDuration) {
            data = null;
            return null;
        } else {
            return data;
        }

    }
}
