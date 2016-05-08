package com.triangleleft.flashcards.service.login.rest.model;

import com.google.gson.annotations.SerializedName;

import com.triangleleft.flashcards.service.common.error.CommonError;
import com.triangleleft.flashcards.service.common.error.ErrorType;
import com.triangleleft.flashcards.util.TextUtils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Model object for server response to /login request
 */
public class LoginResponseModel {
    /*package*/ final static String RESPONSE_OK = "OK";
    /*package*/ final static String FAILURE_LOGIN = "login";
    /*package*/ final static String FAILURE_PASSWORD = "password";

    private static final Map<FailureReason, ErrorType> errorMap = new HashMap<>();

    static {
        errorMap.put(FailureReason.WRONG_LOGIN, ErrorType.LOGIN);
        errorMap.put(FailureReason.WRONG_PASSWORD, ErrorType.PASSWORD);
    }

    @SerializedName("response")
    public String response;
    @SerializedName("username")
    public String userName;
    @SerializedName("user_id")
    public String userId;
    @SerializedName("failure")
    public String failure;
    @SerializedName("message")
    public String message;

    public boolean isSuccess() {
        return RESPONSE_OK.equals(response) && !TextUtils.isEmpty(userId);
    }

    @Nullable
    public FailureReason getFailureReason() {
        return FailureReason.fromString(failure);
    }

    @NonNull
    public CommonError buildError() {
        if (isSuccess()) {
            throw new IllegalStateException("Can't build error for successful result");
        } else {
            ErrorType type = errorMap.get(getFailureReason());
            if (type == null) {
                // No map for type, say that it's internal error
                // Should we fail here for debug builds?
                type = ErrorType.INTERNAL;
            }
            return CommonError.create(type, message);
        }
    }

    public enum FailureReason {
        WRONG_LOGIN(FAILURE_LOGIN),
        WRONG_PASSWORD(FAILURE_PASSWORD);

        private final String reason;

        FailureReason(@NonNull String reason) {
            this.reason = reason;
        }

        @Nullable
        public static FailureReason fromString(@Nullable String field) {
            for (FailureReason value : values()) {
                if (value.reason.equals(field)) {
                    return value;
                }
            }
            return null;
        }
    }

}
