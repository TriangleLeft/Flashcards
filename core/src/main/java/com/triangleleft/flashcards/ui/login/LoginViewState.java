package com.triangleleft.flashcards.ui.login;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class LoginViewState implements ViewState {
    public abstract String login();

    public abstract String password();

    public abstract boolean hasLoginError();

    public abstract boolean hasPasswordError();

    public abstract String genericError();

    public abstract boolean loginButtonEnabled();

    public abstract boolean shouldRememberUser();

    public abstract Page page();

    public enum Page {
        PROGRESS,
        CONTENT,
        SUCCESS
    }

    public abstract Builder asBuilder();

    public static Builder builder() {
        return new AutoValue_LoginViewState.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        public abstract Builder login(String login);

        public abstract Builder password(String password);

        public abstract Builder hasLoginError(boolean hasLoginError);

        public abstract Builder hasPasswordError(boolean hasPasswordError);

        public abstract Builder loginButtonEnabled(boolean loginButtonEnabled);

        public abstract Builder shouldRememberUser(boolean shouldRememberUser);

        public abstract Builder page(Page page);

        public abstract Builder genericError(String genericError);

        public abstract LoginViewState build();
    }

}