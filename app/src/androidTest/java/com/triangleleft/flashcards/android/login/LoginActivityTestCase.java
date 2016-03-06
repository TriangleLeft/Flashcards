package com.triangleleft.flashcards.android.login;

import com.triangleleft.flashcards.FlashcardsApplication;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.error.ErrorType;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.provider.IListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTestCase {

    @Inject
    ILoginModule module;

    @Captor
    ArgumentCaptor<IListener<ILoginResult>> listenerCaptor;

    @Captor
    ArgumentCaptor<ILoginRequest> requestCaptor;

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class, true, false);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        FlashcardsApplication app
                = (FlashcardsApplication) instrumentation.getTargetContext().getApplicationContext();
        module = app.getComponent().loginModule();
    }

    @Test
    public void networkError() throws InterruptedException {
        LoginActivity activity = activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));

        // Verify login form is shown
        onView(withId(R.id.login)).check(matches(isDisplayed()));


        String message = "fail";
        CommonError error = new CommonError(ErrorType.NETWORK, message);
//        doAnswer(invocation -> {
//            IListener<ILoginResult> listener = listenerCaptor.getValue();
//            listener.onFailure(error);
//            return null;
//        }).when(module).doRequest(requestCaptor.capture(), listenerCaptor.capture());

        onView(withId(R.id.login)).perform(typeText("login"));
        onView(withId(R.id.password)).perform(typeText("password"));
        onView(withText(R.string.action_sign_in)).perform(click());

        // Verify progress is shown
        onView(withId(R.id.login_progress)).check(matches(isDisplayed()));


        //Thread.sleep(5000);
        // Check that toast is shown
//        onView(withText(message))
//                .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
//                .check(matches(isDisplayed()));
    }


}
