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

package com.triangleleft.flashcards.rule;

import com.triangleleft.flashcards.FlashcardsApp;
import com.triangleleft.flashcards.util.TextUtils;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.TimeOutDuration;
import okhttp3.mockwebserver.MockResponse;

public class AppiumRule implements TestRule {

    public static final int TIMEOUT = 5;
    private static final String PLATFORM_KEY = "com.triangleleft.flashcards.platform";
    private static final String PLATFORM_ANDROID = "ANDROID";
    private static final String PLATFORM_IOS = "IOS";
    private static final String remoteAddress = "http://127.0.0.1:4723/wd/hub";
    private final MockWebServerRule webServerRule = new MockWebServerRule();
    private AppiumDriver<WebElement> driver;
    private FlashcardsApp app;
    private final boolean fullReset;
    private final TimeOutDuration timeOutDuration = new TimeOutDuration(TIMEOUT, TimeUnit.SECONDS);
    private AppiumFieldDecorator decorator;

    public AppiumRule(boolean fullReset) {
        this.fullReset = fullReset;
    }

    public AppiumDriver<WebElement> getDriver() {
        return driver;
    }

    public AppiumFieldDecorator getDecorator() {
        return decorator;
    }

    public void enqueue(String path, String response) {
        webServerRule.enqueue(path, response);
    }

    public void enqueue(String path, MockResponse response) {
        webServerRule.enqueue(path, response);
    }

    public TimeOutDuration getTimeOutDuration() {
        return timeOutDuration;
    }

    public FlashcardsApp getApp() {
        return app;
    }

    private void before() throws MalformedURLException {
        String platform = System.getProperty(PLATFORM_KEY);
        if (TextUtils.isEmpty(platform)) {
            platform = PLATFORM_ANDROID;
            //throw new IllegalArgumentException("Platform key (" + PLATFORM_KEY + ") was not set!");
        }
        URL remoteURL = new URL(remoteAddress);
        switch (platform) {
            case PLATFORM_ANDROID:
                driver = AppiumAndroidDriver.create(remoteURL, fullReset);
                break;
            case PLATFORM_IOS:
            default:
                throw new IllegalArgumentException("Unknown platform key :" + platform);
        }
        driver.rotate(ScreenOrientation.PORTRAIT);
        decorator = new AppiumFieldDecorator(driver, timeOutDuration);
        app = new FlashcardsApp(decorator);
    }

    private void after() {
        driver.quit();
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return webServerRule.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before();
                try {
                    base.evaluate();
                } finally {
                    after();
                }
            }
        }, description);
    }

}
