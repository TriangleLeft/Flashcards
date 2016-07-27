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

package com.triangleleft.flashcards;

import static com.triangleleft.flashcards.TestUtils.isEmpty;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.triangleleft.flashcards.page.LoginPage;
import com.triangleleft.flashcards.page.MainPage;
import com.triangleleft.flashcards.rule.AppiumAndroidRule;
import com.triangleleft.flashcards.rule.AppiumRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LoginTest {
    public static final String LOGIN = "appiumtest";
    public static final String PASSWORD = "appiumtest";

    @Rule
    public AppiumRule appium = new AppiumAndroidRule(true);

    @Test
    public void login() throws InterruptedException {
        LoginPage loginPage = appium.getApp().loginPage();
        // Should start empty
        assertThat(loginPage.email, isEmpty());
        assertThat(loginPage.password, isEmpty());
        assertThat(loginPage.checkbox.isSelected(), is(false));
        // Enter data
        loginPage.email.sendKeys(LOGIN);
        loginPage.password.sendKeys(PASSWORD);
        loginPage.checkbox.click();
        // Login
        loginPage.buttonSignIn.click();

        // Wait till we can get main page
        MainPage page = appium.getApp().mainPage();
        assertNotNull(page.title.isDisplayed());
    }

}
