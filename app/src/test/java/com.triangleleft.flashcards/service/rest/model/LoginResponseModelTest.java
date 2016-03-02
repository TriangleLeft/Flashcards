package com.triangleleft.flashcards.service.rest.model;

import android.support.annotation.NonNull;

public class LoginResponseModelTest {

    @NonNull
    public static LoginResponseModel buildSuccessModel(@NonNull String userId) {
        LoginResponseModel model = new LoginResponseModel();
        model.userId = userId;
        model.response = LoginResponseModel.RESPONSE_OK;
        return model;
    }

    @NonNull
    public static LoginResponseModel buildLoginFailureModel() {
        LoginResponseModel model = new LoginResponseModel();
        model.failure = LoginResponseModel.FAILURE_LOGIN;
        model.message = "Failed login";
        return model;
    }

    @NonNull
    public static LoginResponseModel buildPasswordFailureModel() {
        LoginResponseModel model = new LoginResponseModel();
        model.failure = LoginResponseModel.FAILURE_PASSWORD;
        model.message = "Failed login";
        return model;
    }
}
