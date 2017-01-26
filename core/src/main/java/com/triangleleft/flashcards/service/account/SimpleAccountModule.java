package com.triangleleft.flashcards.service.account;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.cards.ReviewDirection;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.util.PersistentStorage;

import android.support.annotation.Nullable;

import javax.inject.Inject;

public class SimpleAccountModule implements AccountModule {

    // TODO: this is getting out of hand, ideally it should be streamlined like get/set with default values
    private static final String KEY_USER_ID = "SimpleAccountModule::userId";
    private static final String KEY_LOGIN = "SimpleAccountModule::login";
    private static final String KEY_REMEMBER_USER = "SimpleAccountModule::rememberUser";
    private static final String KEY_USERDATA = "SimpleAccountModule::userData";
    private static final String KEY_WORDS_AMOUNT = "SimpleAccountModule::wordsAmount";
    private static final String KEY_REVIEW_DIRECTION = "SimpleAccountModule::reviewDirection";
    private static final boolean DEFAULT_REMEMBER_USER = false;
    private static final int DEFAULT_FLASHCARDS_COUNT = 15;
    private static final ReviewDirection DEFAULT_REVIEW_DIRECTION = ReviewDirection.FORWARD;
    private final PersistentStorage storage;
    private String userId;
    private String login;
    private UserData userData;
    private Boolean rememberUser;
    private Integer wordsAmount;
    private ReviewDirection reviewDirection;

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
            rememberUser = storage.get(KEY_REMEMBER_USER, Boolean.class, DEFAULT_REMEMBER_USER);
        }
        return rememberUser;
    }

    @Override
    public void setWordsAmount(int wordsAmount) {
        this.wordsAmount = wordsAmount;
        storage.put(KEY_WORDS_AMOUNT, wordsAmount);
    }

    @Override
    public int getWordsAmount() {
        if (wordsAmount == null) {
            wordsAmount = storage.get(KEY_WORDS_AMOUNT, Integer.class, DEFAULT_FLASHCARDS_COUNT);
        }
        return wordsAmount;
    }

    @Override
    public void setWordsReviewDirection(ReviewDirection direction) {
        reviewDirection = direction;
        storage.put(KEY_REVIEW_DIRECTION, direction);
    }

    @Override
    public ReviewDirection getWordsReviewDirection() {
        if (reviewDirection == null) {
            reviewDirection = storage.get(KEY_REVIEW_DIRECTION, ReviewDirection.class, DEFAULT_REVIEW_DIRECTION);
        }
        return reviewDirection;
    }
}
