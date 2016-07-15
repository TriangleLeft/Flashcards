package com.triangleleft.flashcards.service.account;

import android.support.annotation.Nullable;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.settings.UserData;

public interface AccountModule {

    void setUserId(@Nullable String userId);

    Optional<String> getUserId();

    void setLogin(@Nullable String login);

    Optional<String> getLogin();

    Optional<UserData> getUserData();

    void setUserData(UserData data);

    boolean shouldRememberUser();

    void setRememberUser(boolean rememberUser);

}
