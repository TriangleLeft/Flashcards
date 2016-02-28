package com.triangleleft.flashcards.service;

import android.support.annotation.NonNull;

public class SimpleLoginRequest implements ILoginRequest {

    private final String login;
    private final String password;
    private final String tag;

    public SimpleLoginRequest(@NonNull String login, @NonNull String password) {
        this.login = login;
        this.password = password;
        this.tag = login + '@' + hashCode();
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getTag() {
        return tag;
    }
}
