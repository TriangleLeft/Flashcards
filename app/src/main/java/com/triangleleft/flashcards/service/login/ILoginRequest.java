package com.triangleleft.flashcards.service.login;

import com.triangleleft.flashcards.service.provider.IProviderRequest;

import android.support.annotation.NonNull;

import java.io.Serializable;

public interface ILoginRequest extends IProviderRequest, Serializable {

    @NonNull
    String getLogin();

    @NonNull
    String getPassword();
}
