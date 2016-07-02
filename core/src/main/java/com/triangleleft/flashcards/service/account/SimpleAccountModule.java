package com.triangleleft.flashcards.service.account;

import com.triangleleft.flashcards.util.PersistentStorage;

import android.support.annotation.Nullable;

import javax.inject.Inject;

public class SimpleAccountModule implements AccountModule {

    private static final String KEY_USER_ID = "SimpleAccountModule::userId";
    private static final String KEY_LOGIN = "SimpleAccountModule::login";
    private static final String KEY_REMEMBER_USER = "SimpleAccountModule::rememberUser";
    private final String login;
    private final PersistentStorage storage;
    private String userId = null;

    @Inject
    public SimpleAccountModule(PersistentStorage storage) {
        this.storage = storage;
        userId = storage.get(KEY_USER_ID, String.class);
        login = storage.get(KEY_LOGIN, String.class);
    }
    @Override
    public void setUserId(@Nullable String userId) {
        this.userId = userId;
        storage.put(KEY_USER_ID, userId);
    }

    @Nullable
    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public boolean shouldRememberUser() {
        return storage.get(KEY_REMEMBER_USER, Boolean.class, false);
    }

    @Override
    public void setRememberUser(boolean rememberUser) {
        storage.put(KEY_REMEMBER_USER, rememberUser);
    }
}
