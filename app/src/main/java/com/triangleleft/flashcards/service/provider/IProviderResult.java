package com.triangleleft.flashcards.service.provider;

import android.support.annotation.Nullable;

public interface IProviderResult<T> {

    @Nullable
    T getResult();
}
