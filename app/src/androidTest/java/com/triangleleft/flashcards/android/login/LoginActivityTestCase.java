package com.triangleleft.flashcards.android.login;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.android.CustomProgressBar;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.error.ErrorType;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.provider.IListener;
import com.triangleleft.flashcards.mvp.login.presenter.ILoginPresenter;
import com.triangleleft.flashcards.mvp.login.view.ILoginView;
import com.triangleleft.flashcards.mvp.login.view.LoginViewState;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.doAnswer;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTestCase {

    private static final Logger logger = LoggerFactory.getLogger(LoginActivityTestCase.class);

    @Captor
    ArgumentCaptor<IListener<ILoginResult>> listenerCaptor;

    @Captor
    ArgumentCaptor<ILoginRequest> requestCaptor;

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class, true, true);

    private ILoginView loginView;
    private ILoginPresenter presenter;

    @BeforeClass
    public static void beforeClass() {
        CustomProgressBar.disabled = true;
    }

    @Before
    public void setUp() {
        logger.debug("setUp() called");
        MockitoAnnotations.initMocks(this);
        presenter = activityRule.getActivity().getComponent().loginPresenter();
        loginView = activityRule.getActivity();

        loginView.setState(LoginViewState.ENTER_CREDENTIAL);
    }

//    @Test
//    public void progressIsShownWhileRequest() {
//        // Verify login form is shown
//        onView(withId(R.id.login)).check(matches(isDisplayed()));
//
//        onView(withId(R.id.login)).perform(typeText("login"));
//        onView(withId(R.id.password)).perform(typeText("password"));
//        onView(withText(R.string.action_sign_in)).perform(click());
//
//        // Verify progress is shown
//        onView(withId(R.id.login_progress)).check(matches(isDisplayed()));
//    }

    @Test
    public void toastIsShownForNetworkError() throws InterruptedException {
        logger.debug("toastIsShownForNetworkError() called");
        String message = "fail";
        CommonError error = new CommonError(ErrorType.NETWORK, message);
        doAnswer(invocation -> {
            loginView.setState(LoginViewState.ENTER_CREDENTIAL);
            loginView.setGenericError(message);
            return null;
        }).when(presenter).onLoginClick();

        onView(withId(R.id.login)).perform(typeText("login"));
        onView(withId(R.id.password)).perform(typeText("password"));
        onView(withText(R.string.action_sign_in)).perform(click());

        // Check that toast is shown
        onView(withText(message))
                .inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }


}
