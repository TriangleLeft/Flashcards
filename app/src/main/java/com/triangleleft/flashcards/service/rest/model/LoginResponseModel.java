package com.triangleleft.flashcards.service.rest.model;

import com.google.gson.annotations.SerializedName;

import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by lekz112 on 08.10.2015.
 */
public class LoginResponseModel {
    private final static String RESPONSE_OK = "OK";

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
    public ErrorField getErrorField() {
        return ErrorField.fromString(failure);
    }

    public static enum ErrorField {
        LOGIN("login"), PASSWORD("password");

        private String field;

        private ErrorField(String field) {
            this.field = field;
        }

        @Nullable
        public static ErrorField fromString(@Nullable String field) {
            for (ErrorField value : values()) {
                if (value.field.equals(field)) {
                    return value;
                }
            }
            return null;
        }
    }

}
