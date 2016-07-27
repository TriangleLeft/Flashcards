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
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class AppiumAndroidRule implements AppiumRule {

    private final boolean fullReset;

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
    public FlashcardsApp getApp() {
        return app;
    }

    private void before() throws MalformedURLException, InterruptedException {
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "../app/build/outputs/apk/");
        File appFile = new File(appDir, "app-debug.apk");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        if (fullReset) {
            capabilities.setCapability("app", appFile.getAbsolutePath());
            //capabilities.setCapability("fullReset", true);
            capabilities.setCapability("noReset", false);
        } else {
            capabilities.setCapability("noReset", true);
        }
        capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, "com.triangleleft.flashcards");
        capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, ".ui.login.LoginActivity");
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.rotate(ScreenOrientation.PORTRAIT);
        app = new FlashcardsApp(driver);
    }

    private void after() {
        driver.quit();
    }
}
