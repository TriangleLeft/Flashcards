package com.triangleleft.flashcards.service.account;

import android.support.annotation.Nullable;

public class SimpleAccountModule implements AccountModule {
    private String userId = null;

    public SimpleAccountModule() {
//        userId = storage.get(KEY_USER_ID, String.class);
//        login = storage.get(KEY_LOGIN, String.class);
    }
    @Override
    public void setUserId(@Nullable String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public String getUserId() {
        return userId;
    }
}
