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

import com.triangleleft.flashcards.page.LoginPage;
import com.triangleleft.flashcards.rule.AppiumRule;
import com.triangleleft.flashcards.service.RestService;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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

            private boolean wasDisplayed;
            private boolean hadText;

            @Override
            public void describeTo(Description description) {
                description.appendText("a element ")
                        .appendText("with text ")
                        .appendValue(text);
            }

            @Override
            protected void describeMismatchSafely(T item, Description mismatchDescription) {
                if (!wasDisplayed) {
                    mismatchDescription.appendText("element wasn't displayed");
                }
                if (!hadText) {
                    if (!wasDisplayed) {
                        mismatchDescription.appendText(" and ");
                    }
                    mismatchDescription.appendText("was \"").appendText(item.getText()).appendText("\"");
                }
            }

            @Override
            protected boolean matchesSafely(T item) {
                wasDisplayed = item.isDisplayed();
                hadText = item.getText().equals(text);
                return wasDisplayed && hadText;
            }
        };
    }

    public static <T extends WebElement> Matcher<T> isDisplayed() {
        return new TypeSafeMatcher<T>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("an element ");
            }

            @Override
            protected void describeMismatchSafely(T item, Description mismatchDescription) {
                mismatchDescription.appendText("was not displayed");
            }

            @Override
            protected boolean matchesSafely(T item) {
                return item.isDisplayed();
            }
        };
    }

    public static void loginWithUserdata(AppiumRule appium, MockJsonResponse userdata) {
        // Prepare wrong password answer
        appium.enqueue(RestService.PATH_LOGIN, MockJsonResponse.LOGIN_SUCCESS);
        // First userdata request - after login
        appium.enqueue(RestService.PATH_USERDATA, userdata);
        // Second - in drawer presenter
        appium.enqueue(RestService.PATH_USERDATA, userdata);
        LoginPage loginPage = appium.getApp().loginPage();
        loginPage.setLogin("login");
        appium.getDriver().getKeyboard().sendKeys(Keys.RETURN);
        loginPage.setPassword("passw");
        appium.getDriver().getKeyboard().sendKeys(Keys.RETURN);
        loginPage.login();
    }


    public static void assertNotVisible(AppiumRule rule, WebElement element) {
        rule.getTimeOutDuration().setTime(0);
        boolean displayed = false;
        try {
            displayed = element.isDisplayed();
        } catch (NoSuchElementException e) {
            // Ignored;
        }
        assertThat(displayed, is(false));
        rule.getTimeOutDuration().setTime(AppiumRule.TIMEOUT);
    }
}
