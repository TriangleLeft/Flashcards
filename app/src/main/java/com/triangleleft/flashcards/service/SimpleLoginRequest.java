package com.triangleleft.flashcards.service;

import android.support.annotation.NonNull;

public class SimpleLoginRequest implements ILoginRequest {

    private final Credentials credentials;
    private final String tag;

    public SimpleLoginRequest(@NonNull Credentials credentials) {
        this.credentials = credentials;
        this.tag = credentials.getLogin() + '@' + hashCode();
    }

    @NonNull
    @Override
    public String getLogin() {
        return credentials.getLogin();
    }

    @NonNull
    @Override
    public String getPassword() {
        return credentials.getPassword();
    }

    @NonNull
    @Override
    public String getTag() {
        return tag;
    }
}
