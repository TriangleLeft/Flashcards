package com.triangleleft.flashcards.service.provider;

import android.support.annotation.NonNull;

public class SimpleProviderResult<T> implements IProviderResult<T> {

    private final T data;

    public SimpleProviderResult(@NonNull final T data) {
        this.data = data;
    }

    @NonNull
    @Override
    public T getResult() {
        return data;
    }
}
