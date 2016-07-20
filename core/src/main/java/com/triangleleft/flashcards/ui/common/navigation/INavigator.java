package com.triangleleft.flashcards.ui.common.navigation;

import android.support.annotation.NonNull;

import com.triangleleft.flashcards.service.common.error.CommonError;

public interface INavigator {

    void onAction(@NonNull INavigationAction action);

    void onError(@NonNull CommonError error);
}
