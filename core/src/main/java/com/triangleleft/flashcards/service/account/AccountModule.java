package com.triangleleft.flashcards.service.account;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.cards.ReviewDirection;
import com.triangleleft.flashcards.service.settings.UserData;

import android.support.annotation.Nullable;

public interface AccountModule {

    void setUserId(@Nullable String userId);

    Optional<String> getUserId();

    void setLogin(@Nullable String login);

    Optional<String> getLogin();

    Optional<UserData> getUserData();

    void setUserData(@Nullable UserData data);

    boolean shouldRememberUser();

    void setRememberUser(boolean rememberUser);

    void setWordsAmount(int wordsAmount);

    int getWordsAmount();

    void setWordsReviewDirection(ReviewDirection direction);

    ReviewDirection getWordsReviewDirection();
}


