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

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.TimeOutDuration;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class AppiumAndroidRule implements AppiumRule {

    private final boolean fullReset;
    private final TimeOutDuration timeOutDuration = new TimeOutDuration(60, TimeUnit.SECONDS);
    private AppiumFieldDecorator decorator;

    public AppiumAndroidRule() {
        fullReset = false;
    }

    public AppiumAndroidRule(boolean fullReset) {
        this.fullReset = fullReset;
    }

    private AndroidDriver<WebElement> driver;
    private FlashcardsApp app;

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before();
                try {
                    base.evaluate();
                } finally {
                    after();
                }
            }
        };
    }

    @Override
    public AppiumDriver<WebElement> getDriver() {
        return driver;
    }

    @Override
    public AppiumFieldDecorator getDecorator() {
        return decorator;
    }

    @Override
    public TimeOutDuration getTimeOutDuration() {
        return timeOutDuration;
    }

    @Override
    public FlashcardsApp getApp() {
        return app;
    }

    private void before() throws MalformedURLException, InterruptedException {
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "../app/build/outputs/apk/");
        File appFile = new File(appDir, "app-local-debug.apk");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5556");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        if (fullReset) {
            capabilities.setCapability(MobileCapabilityType.APP, appFile.getAbsolutePath());
            //capabilities.setCapability("fullReset", true);
            capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
        } else {
            capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        }
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.triangleleft.flashcards");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".ui.login.LoginActivity");
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.rotate(ScreenOrientation.PORTRAIT);
        decorator = new AppiumFieldDecorator(driver, timeOutDuration);
        app = new FlashcardsApp(decorator);
    }

    private void after() {
        driver.quit();
    }
}
