package com.triangleleft.flashcards.cards;

import com.triangleleft.flashcards.MockWebServerRule;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.test.MockJsonResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class FlashcardsActivityTest {

    private static final Logger logger = LoggerFactory.getLogger(FlashcardsActivityTest.class);
    //    @ClassRule
//    public static DisableAnimationsRule animationsRule = new DisableAnimationsRule();

    @Rule
    public MockWebServerRule webServerRule = new MockWebServerRule();

    @Rule
    public ActivityTestRule<FlashcardsActivity> activityRule =
            new ActivityTestRule<>(FlashcardsActivity.class, true, false);

    private MockWebServer webServer;

    @Before
    public void before() throws IOException {
        logger.debug("before() called");
        webServer = webServerRule.getWebServer();
    }

    @Test
    public void testStartsWithProgress() {
        activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));

        onView(withId(R.id.progress_wheel)).check(matches(isDisplayed()));
    }

    @Test
    @MockJsonResponse("flashcards_valid_response.json")
    public void testCardsCanBeFlipped() {
        activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));

        // First card is shown
        onView(withText("sino")).check(matches(isDisplayed()));
        // Click it
        onView(withId(R.id.deckView)).perform(click());
        // Check that translation is shown
        onView(withText("but")).check(matches(isDisplayed()));
    }

//    @Test
//    @MockJsonResponse("flashcards_valid_response.json")
//    public void testCardsCantBeSwipedWithoutFlippingIt() {
//        activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));
//
//        // First card is shown
//        onView(withText("sino")).check(matches(isDisplayed()));
//        // Try to swipe it
//        onView(withId(R.id.deckView)).perform(swipeRight());
//        // Check that it's still same card
//        onView(withText("sino")).check(matches(isDisplayed()));
//    }
//
//
//    @Test
//    @MockJsonResponse("flashcards_valid_response.json")
//    public void testCardsCantBeSwiped() {
//        activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));
//
//        // First card is shown
//        onView(withText("sino")).check(matches(isDisplayed()));
//        // Click it
//        onView(withId(R.id.deckView)).perform(click());
//        // Swipe it
//        onView(withId(R.id.deckView)).perform(swipeRight());
//        // Check that next card is shown
//        onView(withText("si")).check(matches(isDisplayed()));
//    }


}