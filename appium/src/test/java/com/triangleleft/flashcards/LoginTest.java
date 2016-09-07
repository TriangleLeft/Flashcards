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

import com.triangleleft.flashcards.page.LoginPage;
import com.triangleleft.flashcards.rule.AppiumAndroidRule;
import com.triangleleft.flashcards.rule.AppiumRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.net.InetAddress;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static com.triangleleft.flashcards.TestUtils.hasText;
import static com.triangleleft.flashcards.TestUtils.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(JUnit4.class)
public class LoginTest {
    public static final String LOGIN = "appiumtest";
    public static final String PASSWORD = "appiumtest";

    public static final HttpUrl MOCK_SERVER_URL = HttpUrl.parse("http://localhost:8080/");

    @Rule
    public AppiumRule appium = new AppiumAndroidRule(true);

    @Test
    public void login() throws InterruptedException, IOException {
        MockWebServer webServer = new MockWebServer();
        webServer.start(InetAddress.getByName(MOCK_SERVER_URL.host()), MOCK_SERVER_URL.port());
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setBody("{\n" +
                "  \"failure\": \"login\",\n" +
                "  \"message\": \"Failed login\"\n" +
                "}");
        webServer.enqueue(response);

        LoginPage loginPage = appium.getApp().loginPage();
        // Should start empty
        assertThat(loginPage.email, isEmpty());
        assertThat(loginPage.password, isEmpty());
        assertThat(loginPage.rememberSwitch.isSelected(), is(false));
        // Enter data
        loginPage.email.sendKeys(LOGIN);
        loginPage.password.sendKeys(PASSWORD);
        loginPage.rememberSwitch.click();
        // Login
        loginPage.buttonSignIn.click();

        assertThat(loginPage.loginError, hasText("Wrong login"));
    }

}
