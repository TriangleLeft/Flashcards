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
import com.triangleleft.flashcards.page.MainPage;
import com.triangleleft.flashcards.rule.AppiumRule;
import com.triangleleft.flashcards.rule.MockWebServerRule;
import com.triangleleft.flashcards.util.MockServerResponse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import okhttp3.mockwebserver.MockWebServer;

import static com.triangleleft.flashcards.util.TestUtils.assertNotVisible;
import static com.triangleleft.flashcards.util.TestUtils.hasText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(JUnit4.class)
public class LoginTest {


    private MockWebServerRule webServerRule = new MockWebServerRule();
    private AppiumRule appium = new AppiumRule(true);
    private MockWebServer webServer = webServerRule.getWebServer();

    @Rule
    public RuleChain ruleChain = RuleChain.outerRule(webServerRule).around(appium);
    private LoginPage loginPage;

    @Test
    public void progressIsShown() {
        loginPage = appium.getApp().loginPage();
        login("login", "pass");

        loginPage = appium.getApp().loginPage();
        // Check that progress bar is shown while request is underway
        assertThat(loginPage.progressBar.isDisplayed(), is(true));
    }

    @Test
    public void loginError() {
        loginPage = appium.getApp().loginPage();
        login("login", "pass");
        // Answer with login error
        webServerRule.enqueue(MockServerResponse.make("login/wrong_login_response.json"));
        loginPage = appium.getApp().loginPage();
        // Check that login error is shown
        assertThat(loginPage.loginError, hasText("Wrong login"));
        // change login
        loginPage.setLogin("newLogin");
        appium.getDriver().hideKeyboard();
        // Error should be hidden
        assertNotVisible(appium, loginPage.loginError);
    }

    @Test
    public void passwordError() {
        loginPage = appium.getApp().loginPage();
        login("login", "pass");
        // Prepare wrong password answer
        webServerRule.enqueue(MockServerResponse.make("login/wrong_password_response.json"));
        loginPage.login();
        // Check password error is shown
        assertThat(loginPage.passwordError, hasText("Wrong password"));
        loginPage.setPassword("newPassw");
        appium.getDriver().hideKeyboard();
        // Error should be hidden
        assertNotVisible(appium, loginPage.passwordError);
    }

    @Test
    public void validLogin() {
        loginPage = appium.getApp().loginPage();
        login("login", "pass");

        // Prepare valid response
        webServerRule.enqueue(MockServerResponse.make("login/valid_response.json"));
        // Prepare userdata with French as current language
        webServerRule.enqueue(MockServerResponse.make("userdata/userdata_french.json"));

        MainPage mainPage = appium.getApp().mainPage();
        assertThat(mainPage.title, hasText("French"));
    }

    private void login(String login, String password) {
        loginPage.setLogin(login);
        loginPage.setPassword(password);
        appium.getDriver().hideKeyboard();
        loginPage.login();
    }

}
