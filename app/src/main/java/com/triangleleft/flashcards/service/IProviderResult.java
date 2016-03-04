package com.triangleleft.flashcards.service;

import android.support.annotation.Nullable;

public interface IProviderResult<T> {

    @Nullable
    T getResult();
}
