package com.triangleleft.flashcards.service.login;

import com.triangleleft.flashcards.service.common.IProviderRequest;

import android.support.annotation.NonNull;

public interface ILoginRequest extends IProviderRequest {

    @NonNull
    String getLogin();

    @NonNull
    String getPassword();
}
