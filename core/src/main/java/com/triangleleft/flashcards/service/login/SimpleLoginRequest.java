package com.triangleleft.flashcards.service.login;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import android.support.annotation.NonNull;

@FunctionsAreNonnullByDefault
public class SimpleLoginRequest implements ILoginRequest {

    private final String tag;
    private final String login;
    private final String passwrd;

    public SimpleLoginRequest(String login, String passwrd) {
        this.login = login;
        this.passwrd = passwrd;
        this.tag = login + '@' + hashCode();
    }

    @NonNull
    @Override
    public String getLogin() {
        return login;
    }

    @NonNull
    @Override
    public String getPassword() {
        return passwrd;
    }

    @NonNull
    @Override
    public String getTag() {
        return tag;
    }
}
