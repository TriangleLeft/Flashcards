package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.login.LoginStatus;
import com.triangleleft.flashcards.service.provider.SimpleProviderResult;

import android.support.annotation.NonNull;

public class ILoginResultImpl extends SimpleProviderResult<LoginStatus> implements ILoginResult {

    public ILoginResultImpl(@NonNull LoginStatus data) {
        super(data);
    }
}
