package com.triangleleft.flashcards.main;

import com.triangleleft.flashcards.MockWebServerRule;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.test.MockJsonResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private MockWebServerRule webServerRule = new MockWebServerRule();

    @Rule
    public RuleChain ruleChain = RuleChain.outerRule(webServerRule)
            .around(new ActivityTestRule<>(MainActivity.class, true, true));

    @Before
    public void before() {

    }

    @Test
    public void testStartsWithProgress() {
        onView(withId(R.id.vocabular_list_progress)).check(matches(isDisplayed()));
    }

    @Test
    @MockJsonResponse("vocabular_valid_response.json")
    public void testVocabularIsShown() {
        onView(withText("camisa")).check(matches(isDisplayed()));
    }

    @Test
    @MockJsonResponse(value = "internal_server_error.txt", errorCode = 500)
    public void testRetryWhenServerReturnedError() {
        onView(withText(R.string.vocabular_list_error_retry)).check(matches(isDisplayed()));

      //  webServerRule.getWebServer().enqueue();
        onView(withText(R.string.button_retry)).perform(click());
    }


}