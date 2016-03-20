package com.triangleleft.flashcards.mvp.main.navigation;

import android.support.annotation.NonNull;

public interface INavigator {

    void navigate(@NonNull INavigationRequest request);
}
