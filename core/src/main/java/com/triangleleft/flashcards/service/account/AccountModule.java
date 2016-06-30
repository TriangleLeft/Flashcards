package com.triangleleft.flashcards.service.account;

import android.support.annotation.Nullable;

public interface AccountModule {

    void setUserId(@Nullable String userId);

    @Nullable
    String getUserId();
}
