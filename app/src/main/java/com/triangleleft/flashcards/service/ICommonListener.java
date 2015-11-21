package com.triangleleft.flashcards.service;

import com.triangleleft.flashcards.service.error.CommonError;

import android.support.annotation.NonNull;

public interface ICommonListener {
    void onSuccess();
    void onError(@NonNull CommonError error);
}
