package com.triangleleft.flashcards.mvp.common.navigation;

import com.triangleleft.flashcards.service.common.error.CommonError;

import android.support.annotation.NonNull;

public interface INavigator {

    void onAction(@NonNull INavigationAction action);

    void onError(@NonNull CommonError error);
}
