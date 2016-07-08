package com.triangleleft.flashcards.service.login.rest.model;

import android.support.annotation.NonNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class LoginResponseModelTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void failBuildErrorForSuccessfulResponse() {
        LoginResponseModel model = buildSuccessModel("123");

        exception.expect(IllegalStateException.class);
        model.buildError();
        fail();
    }


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
        model.failureReason = LoginResponseModel.FAILURE_LOGIN;
        model.message = "Failed login";
        return model;
    }

    @NonNull
    public static LoginResponseModel buildPasswordFailureModel() {
        LoginResponseModel model = new LoginResponseModel();
        model.failureReason = LoginResponseModel.FAILURE_PASSWORD;
        model.message = "Failed login";
        return model;
    }

    @NonNull
    public static LoginResponseModel buildUnknownFailureModel() {
        LoginResponseModel model = new LoginResponseModel();
        model.failureReason = "unknown";
        model.message = "Failed login";
        return model;
    }
}
