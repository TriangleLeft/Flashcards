package com.triangleleft.flashcards.login;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.triangleleft.flashcards.MockFlashcardsApplication;
import com.triangleleft.flashcards.MockWebServerRule;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.test.MockServerResponse;
import com.triangleleft.flashcards.util.PersistentStorage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.triangleleft.flashcards.test.EspressoUtils.checkHasView;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTestCase {

    private static final Logger logger = LoggerFactory.getLogger(LoginActivityTestCase.class);

    private MockWebServerRule webServerRule = new MockWebServerRule();

    private ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class, true, false);

    @Rule
    public RuleChain ruleChain = RuleChain.outerRule(webServerRule)
            .around(activityTestRule);

    private MockWebServer webServer = webServerRule.getWebServer();


    @Before
    public void setUp() {
        logger.debug("setUp() called");
        MockitoAnnotations.initMocks(this);

        PersistentStorage storage = MockFlashcardsApplication.getInstance().getComponent().persistentStorage();
        storage.put("SimpleAccountModule::rememberUser", false);
        activityTestRule.launchActivity(new Intent(Intent.ACTION_MAIN));
    }

    @Test
    public void progressIsShownWhileRequest() {
        onView(withId(R.id.login_email)).perform(typeText("login"));
        onView(withId(R.id.login_password)).perform(typeText("password")).perform(closeSoftKeyboard());
        onView(withText(R.string.action_sign_in)).perform(click());

        // Verify progress is shown
        checkHasView(withId(R.id.login_progress));

        webServer.enqueue(MockServerResponse.make("login/valid_response.json"));
    }

    @Test
    public void toastIsShownForNetworkError() throws InterruptedException {
        logger.debug("toastIsShownForNetworkError() called");
        String message = "fail";
//        CommonError error = new CommonError(ErrorType.NETWORK, message);
//        doAnswer(invocation -> {
//            loginView.setState(LoginViewStatePage.ENTER_CREDENTIAL);
//            loginView.setGenericError(message);
//            return null;
//        }).when(presenter).onLoginClick();
//
//        onView(withId(R.id.login)).perform(typeText("login"));
//        onView(withId(R.id.password)).perform(typeText("password"));
//        onView(withText(R.string.action_sign_in)).perform(click());
//
//        // Check that toast is shown
//        onView(withText(message))
//                .inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView())))
//                .check(matches(isDisplayed()));
    }


}
