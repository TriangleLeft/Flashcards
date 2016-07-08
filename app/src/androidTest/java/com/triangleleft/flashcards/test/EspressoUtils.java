package com.triangleleft.flashcards.test;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;

public class EspressoUtils {
    public static Matcher<View> visible(Matcher<View> matcher) {
        return Matchers.allOf(matcher, isCompletelyDisplayed());
    }

    /**
     * Check that view with provided matcher is displayed.
     *
     * @param viewMatcher matcher to match
     */
    public static void checkHasView(Matcher<View> viewMatcher) {
        Espresso.onView(viewMatcher).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

}
