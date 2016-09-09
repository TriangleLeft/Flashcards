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

package com.triangleleft.flashcards.test;

import com.triangleleft.flashcards.page.LoginPage;
import com.triangleleft.flashcards.rule.AppiumAndroidRule;
import com.triangleleft.flashcards.rule.AppiumRule;
import com.triangleleft.flashcards.rule.MockWebServerRule;
import com.triangleleft.flashcards.util.MockJsonResponse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.triangleleft.flashcards.util.TestUtils.hasText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(JUnit4.class)
public class LoginTest {


    private MockWebServerRule webServerRule = new MockWebServerRule();
    private AppiumRule appium = new AppiumAndroidRule(true);

    @Rule
    public RuleChain ruleChain = RuleChain.outerRule(webServerRule).around(appium);
    private LoginPage loginPage;

    @Test
    @MockJsonResponse("login/wrong_login_response.json")
    public void wrongLogin() {
        loginPage = appium.getApp().loginPage();

        loginPage.login("login", "password");

        assertThat(loginPage.loginError, hasText("Wrong login"));
    }

    @Test
    @MockJsonResponse("login/wrong_password_response.json")
    public void wrongPassword() {
        loginPage = appium.getApp().loginPage();

        loginPage.login("login", "password");

        assertThat(loginPage.passwordError, hasText("Wrong password"));
    }

    @Test
    public void progressIsShown() {
        loginPage = appium.getApp().loginPage();
        loginPage.login("login", "password");

        loginPage = appium.getApp().loginPage();
        assertThat(loginPage.progressBar.isDisplayed(), is(true));
    }

}
