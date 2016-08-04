package com.triangleleft.flashcards.service.login.rest.model;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.triangleleft.flashcards.service.common.exception.ServerException;
import com.triangleleft.flashcards.service.login.exception.LoginException;
import com.triangleleft.flashcards.service.login.exception.PasswordException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LoginResponseModelTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private LoginResponseModel model;

    @Before
    public void before() {
        model = new LoginResponseModel();
    }

    @Test
    public void success() {
        model.response = LoginResponseModel.RESPONSE_OK;
        model.userId = "id";
        assertThat(model.isSuccess(), is(true));
    }

    @Test
    public void wrongResponse() {
        model.response = "response";
        model.userId = "id";
        assertThat(model.isSuccess(), is(false));
    }

    @Test
    public void noUserId() {
        model.response = LoginResponseModel.RESPONSE_OK;
        model.userId = "";
        assertThat(model.isSuccess(), is(false));
    }

    @Test
    public void getUserId() {
        model.userId = "id";
        assertThat(model.getUserId(), is("id"));
    }

    @Test
    public void getErrorFailForSuccess() {
        model.response = LoginResponseModel.RESPONSE_OK;
        model.userId = "id";

        exception.expect(IllegalStateException.class);
        //noinspection ThrowableResultOfMethodCallIgnored
        model.getError();
    }

    @Test
    public void emptyFailureReason() {
        model.failureReason = "";
        assertThat(model.getError(), instanceOf(ServerException.class));
    }

    @Test
    public void failureLogin() {
        model.failureReason = LoginResponseModel.FAILURE_LOGIN;
        assertThat(model.getError(), instanceOf(LoginException.class));
    }

    @Test
    public void failurePassword() {
        model.failureReason = LoginResponseModel.FAILURE_PASSWORD;
        assertThat(model.getError(), instanceOf(PasswordException.class));
    }

    @Test
    public void failureUnknown() {
        model.failureReason = "unknown";
        assertThat(model.getError(), instanceOf(ServerException.class));
    }
}
