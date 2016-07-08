package com.triangleleft.flashcards.service.login.rest.model;

import com.google.gson.annotations.SerializedName;
import com.triangleleft.flashcards.service.common.exception.ServerException;
import com.triangleleft.flashcards.service.login.exception.LoginException;
import com.triangleleft.flashcards.service.login.exception.PasswordException;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.TextUtils;

/**
 * Model object for server response to /login request
 */
@FunctionsAreNonnullByDefault
public class LoginResponseModel {
    /*package*/ final static String RESPONSE_OK = "OK";
    /*package*/ final static String FAILURE_LOGIN = "login";
    /*package*/ final static String FAILURE_PASSWORD = "password";

    @SerializedName("response")
    public String response;
    @SerializedName("username")
    public String userName;
    @SerializedName("user_id")
    public String userId;
    @SerializedName("failure")
    public String failureReason;
    @SerializedName("message")
    public String message;

    public boolean isSuccess() {
        return RESPONSE_OK.equals(response) && !TextUtils.isEmpty(userId);
    }

    public Exception buildError() {
        if (!isSuccess()) {
            if (TextUtils.hasText(failureReason)) {
                switch (failureReason) {
                    case FAILURE_LOGIN:
                        return new LoginException(failureReason);
                    case FAILURE_PASSWORD:
                        return new PasswordException(failureReason);
                }
            }
            return new ServerException();
        } else {
            throw new IllegalStateException("Can't build error for successful result");
        }
    }
}
