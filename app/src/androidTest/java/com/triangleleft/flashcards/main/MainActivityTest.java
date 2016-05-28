package com.triangleleft.flashcards.main;

import com.triangleleft.flashcards.MockWebServerRule;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.test.MockJsonResponse;
import com.triangleleft.flashcards.test.MockServerResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import android.annotation.SuppressLint;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private MockWebServerRule webServerRule = new MockWebServerRule();

    @Rule
    public RuleChain ruleChain = RuleChain.outerRule(webServerRule)
            .around(new ActivityTestRule<>(MainActivity.class, true, true));

    private MockWebServer webServer = webServerRule.getWebServer();

    @Before
    public void before() {

    }

    @Test
    public void testStartsWithProgress() {
        onView(withId(R.id.vocabular_list_progress)).check(matches(isDisplayed()));
        webServer.enqueue(MockServerResponse.make("vocabular_valid_response.json"));
    }

    @Test
    @MockJsonResponse("vocabular_valid_response.json")
    public void testVocabularIsShown() {
        onView(withText("word1")).check(matches(isDisplayed()));
        onView(withText("word2")).check(matches(isDisplayed()));
        onView(withText("word3")).check(matches(isDisplayed()));
    }

    @Test
    @MockJsonResponse(value = "internal_server_error.txt", httpCode = 500)
    public void testRetryWhenServerReturnedError() {
        onView(withText(R.string.vocabular_list_error_retry)).check(matches(isDisplayed()));

        webServer.enqueue(MockServerResponse.make("vocabular_valid_response.json"));
        onView(withText(R.string.button_retry)).perform(click());
    }

    @Test
    @MockJsonResponse("vocabular_valid_response.json")
    public void testClickingWordWouldShowInfo() {
        onView(withText("word1")).perform(click());

        // TODO: obtain translation?
    }

    @Test
    @MockJsonResponse("vocabular_valid_response.json")
    public void testInfoCanBeClosedByBackPress() {
        onView(withText("word1")).perform(click());

        pressBack();
        // Check that some other word from the list if visible
        onView(withText("word2")).check(matches(isDisplayed()));
    }

    @SuppressLint("PrivateResource")
    @Test
    @MockJsonResponse("vocabular_valid_response.json")
    public void testInfoCanBeClosedByArrowClick() {
        onView(withText("word1")).perform(click());
        // Press arrow icon
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
    }

    public void testPullToRefresh() {

    }


}