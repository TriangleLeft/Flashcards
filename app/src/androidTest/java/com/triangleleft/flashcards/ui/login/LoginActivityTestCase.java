package com.triangleleft.flashcards.ui.login;

import com.triangleleft.flashcards.MockFlashcardsApplication;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.util.PersistentStorage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTestCase {

    private static final Logger logger = LoggerFactory.getLogger(LoginActivityTestCase.class);

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class, true, false);

    @Before
    public void setUp() {
        logger.debug("setUp() called");
        MockitoAnnotations.initMocks(this);

        PersistentStorage storage = MockFlashcardsApplication.getInstance().getComponent().persistentStorage();
        storage.put("SimpleAccountModule::rememberUser", false);
        activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));
    }



    @Test
    public void toastIsShownForNetworkError() throws InterruptedException {
        enterCredentials();

        // Check that toast is shown
        onView(withText(R.string.login_network_error))
                .inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void toastIsShownForGenericError() throws InterruptedException {
        enterCredentials();

        // Check that toast is shown
        onView(withText(R.string.login_generic_error))
                .inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    private void enterCredentials() {
        onView(withId(R.id.login_email)).perform(typeText("login"));
        onView(withId(R.id.login_password)).perform(typeText("password")).perform(closeSoftKeyboard());
        onView(withText(R.string.action_sign_in)).perform(click());
    }

}
