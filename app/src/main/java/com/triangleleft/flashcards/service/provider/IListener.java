package com.triangleleft.flashcards.service.provider;

import com.triangleleft.flashcards.service.error.CommonError;

import android.support.annotation.NonNull;

public interface IListener<Result extends IProviderResult<?>> {

    void onResult(@NonNull Result result);

    void onFailure(@NonNull CommonError error);
}
