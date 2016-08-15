package com.triangleleft.flashcards.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Response;

public class NetworkDelayInterceptor implements Interceptor {

    private final long delay;
    private final TimeUnit unit;

    public NetworkDelayInterceptor(long delay, TimeUnit unit) {
        this.delay = delay;
        this.unit = unit;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            unit.sleep(delay);
        } catch (InterruptedException e) {
            // Ignore
        }
        return chain.proceed(chain.request());
    }
}
