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
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.util.ResourcesUtils;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.triangleleft.flashcards.util.TestUtils.assertNotVisible;
import static com.triangleleft.flashcards.util.TestUtils.hasText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(JUnit4.class)
public class LoginTest {

    @Rule
    public AppiumRule appium = new AppiumRule(true);
    private LoginPage loginPage;

    @Ignore
    @Test
    public void progressIsShown() {
        loginPage = appium.getApp().loginPage();
        // FXIME: enqueue delay?
        login("login", "pass");
        // Reload page to find progress bar
        loginPage = appium.getApp().loginPage();
        // Check that progress bar is shown while request is underway
        assertThat(loginPage.progressBar.isDisplayed(), is(true));
    }

    @Test
    public void loginError() {
        loginPage = appium.getApp().loginPage();
        // Prepare login error
        appium.enqueue(RestService.PATH_LOGIN, ResourcesUtils.LOGIN_WRONG_LOGIN);
        login("login", "pass");
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
        // Prepare wrong password answer
        appium.enqueue(RestService.PATH_LOGIN, ResourcesUtils.LOGIN_WRONG_PASSWORD);
        login("login", "pass");
        // Check password error is shown
        assertThat(loginPage.passwordError, hasText("Wrong password"));
        loginPage.setPassword("newPassw");
        appium.getDriver().hideKeyboard();
        // Error should be hidden
        assertNotVisible(appium, loginPage.passwordError);
    }

    @Test
    public void validLogin() {
        // Prepare valid response
        appium.enqueue(RestService.PATH_LOGIN, ResourcesUtils.LOGIN_SUCCESS);
        // Prepare userdata with French as current language
        appium.enqueue(RestService.PATH_USERDATA, ResourcesUtils.USERDATA_FRENCH);

        loginPage = appium.getApp().loginPage();
        login("login", "pass");

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
