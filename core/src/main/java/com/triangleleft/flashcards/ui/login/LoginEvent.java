package com.triangleleft.flashcards.ui.login;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class LoginEvent {

    public abstract String login();

    public abstract String password();

    public static LoginEvent create(String login, String password) {
        return new AutoValue_LoginEvent(login, password);
    }
}
