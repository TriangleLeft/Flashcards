package com.triangleleft.flashcards.service.login.rest;

import com.google.gson.annotations.SerializedName;
import com.triangleleft.flashcards.service.common.exception.ServerException;
import com.triangleleft.flashcards.service.login.exception.LoginException;
import com.triangleleft.flashcards.service.login.exception.PasswordException;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.TextUtils;

@FunctionsAreNonnullByDefault
public class LoginResponseModel {
    /*package*/ final static String RESPONSE_OK = "OK";
    /*package*/ final static String FAILURE_LOGIN = "login";
    /*package*/ final static String FAILURE_PASSWORD = "password";

    @SerializedName("response")
    String response;
    @SerializedName("username")
    String userName;
    @SerializedName("user_id")
    String userId;
    @SerializedName("failure")
    String failureReason;
    @SerializedName("message")
    String message;

    public boolean isSuccess() {
        return RESPONSE_OK.equals(response) && TextUtils.hasText(userId) && TextUtils.isEmpty(failureReason);
    }

    public String getUserId() {
        return userId;
    }

    public Exception getError() {
        if (!isSuccess()) {
            switch (failureReason) {
                case FAILURE_LOGIN:
                    return new LoginException();
                case FAILURE_PASSWORD:
                    return new PasswordException();
                default:
                    return new ServerException("Unknown failure reason: " + failureReason);
            }
        } else {
            throw new IllegalStateException("Can't build error for successful result");
        }
    }
}
