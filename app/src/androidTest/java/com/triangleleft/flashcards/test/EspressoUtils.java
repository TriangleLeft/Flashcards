package com.triangleleft.flashcards.test;

import org.hamcrest.Matcher;

import android.view.View;

import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;

public class EspressoUtils {
    public static Matcher<View> visible(Matcher<View> matcher) {
        return allOf(matcher, isCompletelyDisplayed());
    }
}
