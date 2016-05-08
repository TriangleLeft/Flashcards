package com.triangleleft.flashcards.service.common;

public class SimpleProviderResult<T> implements IProviderResult<T> {

    private final T data;

    public SimpleProviderResult(final T data) {
        this.data = data;
    }

    @Override
    public T getResult() {
        return data;
    }
}
