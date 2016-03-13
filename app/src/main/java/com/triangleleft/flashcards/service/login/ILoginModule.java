package com.triangleleft.flashcards.service.login;

import com.triangleleft.flashcards.service.provider.IProvider;

import android.support.annotation.NonNull;

public interface ILoginModule extends IProvider<ILoginRequest, ILoginResult> {

    @NonNull
    LoginStatus getLoginStatus();

}
