package com.triangleleft.flashcards.test;

import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
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

    public static Matcher<View> withError(@StringRes final int resourceId) {
        return new BoundedMatcher<View, EditText>(EditText.class) {

            public String resourceName;
            public String expectedText;

            @Override
            public boolean matchesSafely(EditText view) {
                if (null == expectedText) {
                    try {
                        expectedText = view.getResources().getString(resourceId);
                        resourceName = view.getResources().getResourceEntryName(resourceId);
                    } catch (Resources.NotFoundException ignored) {
                        /* view could be from a context unaware of the resource id. */
                    }
                }
                if (expectedText != null && view.getError() != null) {
                    return expectedText.equals(view.getError().toString());
                } else {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with string from resource id: ");
                description.appendValue(resourceId);
                if (null != resourceName) {
                    description.appendText("[");
                    description.appendText(resourceName);
                    description.appendText("]");
                }
                if (null != expectedText) {
                    description.appendText(" value: ");
                    description.appendText(expectedText);
                }
            }
        };
    }

}
