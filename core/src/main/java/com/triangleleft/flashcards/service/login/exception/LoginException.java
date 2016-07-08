/*
 *
 * =======================================================================
 *
 * Copyright (c) 2014-2015 Domlex Limited. All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Domlex Limited.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Domlex Limited.
 *
 * =======================================================================
 *
 */

package com.triangleleft.flashcards.service.login.exception;

import android.support.annotation.NonNull;

import com.annimon.stream.Objects;

public class LoginException extends RuntimeException {

    public LoginException(@NonNull String message) {
        super(message);
        Objects.requireNonNull(message);
    }

    @NonNull
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
