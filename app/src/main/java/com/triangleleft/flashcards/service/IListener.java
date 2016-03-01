package com.triangleleft.flashcards.service;

import android.support.annotation.NonNull;

public interface IListener<Result extends IProviderResult<?>> {
    void onResult(@NonNull Result result);
}
