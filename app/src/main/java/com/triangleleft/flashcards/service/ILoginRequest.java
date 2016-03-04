package com.triangleleft.flashcards.service;

import android.support.annotation.NonNull;

public interface ILoginRequest extends IProviderRequest {

    @NonNull
    String getLogin();

    @NonNull
    String getPassword();
}
