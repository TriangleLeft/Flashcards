package com.triangleleft.flashcards.cards;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.triangleleft.flashcards.test.EspressoUtils.visible;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.triangleleft.flashcards.DisableAnimationsRule;
import com.triangleleft.flashcards.MockWebServerRule;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.test.MockJsonResponse;
import com.triangleleft.flashcards.test.MockServerResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class FlashcardsActivityTest {

    private static final Logger logger = LoggerFactory.getLogger(FlashcardsActivityTest.class);
    @ClassRule
    public static DisableAnimationsRule animationsRule = new DisableAnimationsRule();
    @Rule
    public MockWebServerRule webServerRule = new MockWebServerRule();
    @Rule
    public ActivityTestRule<FlashcardsActivity> activityRule = new ActivityTestRule<>(FlashcardsActivity.class, true,
        false);

    private MockWebServer webServer;

    @Before
    public void before() throws IOException {
        logger.debug("before() called");
        webServer = webServerRule.getWebServer();
        activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));
    }

    @Test
    public void testStartsWithProgress() {
        onView(withId(R.id.progress_wheel)).check(matches(isDisplayed()));
        // Have to send some response, else we would wait 5 seconds for timeout
        webServer.enqueue(MockServerResponse.make("flashcards_valid_response.json"));
    }

    @Test
    @MockJsonResponse("flashcards_valid_response.json")
    public void testCardsCanBeFlipped() {
        // Click on card
        onView(withText("word1")).perform(click());
        // Check that translation is shown
        onView(withText("translation1")).check(matches(isDisplayed()));
    }

    @Test
    @MockJsonResponse("flashcards_valid_response.json")
    public void testNextCardIsShown() {
        // Click flashcard
        onView(withText("word1")).perform(click());
        // Click "I was right"
        onView(visible(withText(R.string.flashcard_button_right))).perform(click());
        // Check that next card is shown
        onView(withText("word2")).check(matches(isDisplayed()));
    }

    @Test
    @MockJsonResponse(value = "internal_server_error.txt", httpCode = 500)
    public void testErrorRetry() {
        // Check error message is shown
        onView(withText(R.string.flashcard_error)).check(matches(isDisplayed()));
        webServer.enqueue(MockServerResponse.make("flashcards_valid_response.json"));
        // Click retry
        onView(withText(R.string.button_retry)).perform(click());
        // Check word is shown
        onView(withText("word1")).check(matches(isDisplayed()));
    }

    @Test
    @MockJsonResponse("flashcards_valid_response.json")
    public void testRestartAfterCardsDepleted() {
        // Click through all cards
        onView(withText("word1")).perform(click());
        onView(visible(withText(R.string.flashcard_button_right))).perform(click());
        onView(withText("word2")).perform(click());
        onView(visible(withText(R.string.flashcard_button_right))).perform(click());
        onView(withText("word3")).perform(click());
        onView(visible(withText(R.string.flashcard_button_right))).perform(click());

        webServer.enqueue(MockServerResponse.make("flashcards_valid_response2.json"));
        onView(withId(R.id.flashcard_button_restart)).perform(click());
        // Check new response is shown
        onView(withText("word4")).check(matches(isDisplayed()));
    }

}