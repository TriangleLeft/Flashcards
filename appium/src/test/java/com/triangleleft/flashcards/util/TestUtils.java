/*
 *
 * =======================================================================
 *
 * Copyright (c) 2014-2015 Domlex Limited. All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Domlex Limited.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Domlex Limited.
 *
 * =======================================================================
 *
 */

package com.triangleleft.flashcards.util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;

public class TestUtils {
    private TestUtils() {
        // static use only
    }

    public static void rotateSafely(AppiumDriver<?> driver, ScreenOrientation orientation) {
        try {
            driver.rotate(orientation);
        } catch (Exception ignored) {
            // For some reason, even though screen is rotated it's reported as error
        }
    }

    public static <T extends WebElement> Matcher<T> isEmpty() {
        return hasText("");
    }

    public static <T extends WebElement> Matcher<T> hasText(final String text) {
        return new TypeSafeMatcher<T>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("a element ")
                    .appendText("with text")
                    .appendValue(text);
            }

            @Override
            protected void describeMismatchSafely(T item, Description mismatchDescription) {
                mismatchDescription.appendText("was \"").appendText(item.getText()).appendText("\"");
            }

            @Override
            protected boolean matchesSafely(T item) {
                return item.getText().equals(text);
            }
        };
    }

}
