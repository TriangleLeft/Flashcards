package com.triangleleft.flashcards.service.account;

import android.support.annotation.Nullable;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.util.PersistentStorage;

import javax.inject.Inject;

public class SimpleAccountModule implements AccountModule {

    private static final String KEY_USER_ID = "SimpleAccountModule::userId";
    private static final String KEY_LOGIN = "SimpleAccountModule::login";
    private static final String KEY_REMEMBER_USER = "SimpleAccountModule::rememberUser";
    private static final String KEY_USERDATA = "SimpleAccountModule::userData";
    private static final boolean DEFAULT_REMEMBER_USER = false;
    private final PersistentStorage storage;
    private String userId;
    private String login;
    private UserData userData;
    private Boolean rememberUser;

    @Inject
    public SimpleAccountModule(PersistentStorage storage) {
        this.storage = storage;
    }

    @Override
    public Optional<String> getUserId() {
        if (userId == null) {
            userId = storage.get(KEY_USER_ID, String.class);
        }
        return Optional.ofNullable(userId);
    }

    @Override
    public void setUserId(@Nullable String userId) {
        this.userId = userId;
        storage.put(KEY_USER_ID, userId);
    }

    @Override
    public Optional<String> getLogin() {
        if (login == null) {
            login = storage.get(KEY_LOGIN, String.class);
        }
        return Optional.ofNullable(login);
    }

    @Override
    public void setLogin(@Nullable String login) {
        this.login = login;
        storage.put(KEY_LOGIN, login);
    }

    @Override
    public Optional<UserData> getUserData() {
        if (userData == null) {
            userData = storage.get(KEY_USERDATA, UserData.class);
        }
        return Optional.ofNullable(userData);
    }

    @Override
    public void setUserData(@Nullable UserData userData) {
        this.userData = userData;
        storage.put(KEY_USERDATA, userData);
    }

    @Override
    public void setRememberUser(boolean rememberUser) {
        this.rememberUser = rememberUser;
        storage.put(KEY_REMEMBER_USER, rememberUser);
    }

    @Override
    public boolean shouldRememberUser() {
        if (rememberUser == null) {
            rememberUser = storage.get(KEY_REMEMBER_USER, Boolean.class);
        }
        return rememberUser == null ? DEFAULT_REMEMBER_USER : rememberUser;
    }
}
