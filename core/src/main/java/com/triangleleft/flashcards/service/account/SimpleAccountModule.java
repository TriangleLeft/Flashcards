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
    private final static String KEY_USERDATA = "SimpleAccountModule::userData";
    private final PersistentStorage storage;


    @Inject
    public SimpleAccountModule(PersistentStorage storage) {
        this.storage = storage;
    }

    @Override
    public void setUserId(@Nullable String userId) {
        storage.put(KEY_USER_ID, userId);
    }

    @Override
    public Optional<String> getUserId() {
        return Optional.ofNullable(storage.get(KEY_USER_ID, String.class));
    }

    @Override
    public void setLogin(@Nullable String login) {
        storage.put(KEY_LOGIN, login);
    }

    @Override
    public Optional<String> getLogin() {
        return Optional.ofNullable(storage.get(KEY_LOGIN, String.class));
    }

    @Override
    public Optional<UserData> getUserData() {
        return Optional.ofNullable(storage.get(KEY_USERDATA, UserData.class));
    }

    @Override
    public void setUserData(@Nullable UserData data) {
        storage.put(KEY_USERDATA, data);
    }

    @Override
    public boolean shouldRememberUser() {
        Boolean result = storage.get(KEY_REMEMBER_USER, Boolean.class);
        return result != null ? result : false;
    }

    @Override
    public void setRememberUser(boolean rememberUser) {
        storage.put(KEY_REMEMBER_USER, rememberUser);
    }
}
