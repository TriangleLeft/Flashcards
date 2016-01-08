package com.triangleleft.flashcards.service;

import android.support.annotation.NonNull;

public interface ILoginModule {

    boolean isLogged();
    void login(@NonNull String login, @NonNull String password);
    void registerListener(@NonNull ICommonListener listener);
    void unregisterListener(@NonNull ICommonListener listener);
}
