package com.triangleleft.flashcards.service.provider;

import android.support.annotation.NonNull;

public interface IProviderResult<T> {

    @NonNull
    T getResult();
}
