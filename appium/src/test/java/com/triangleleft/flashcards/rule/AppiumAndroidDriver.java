package com.triangleleft.flashcards.rule;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class AppiumAndroidDriver extends AndroidDriver<WebElement> {

    public static final String APK_PATH = "../app/build/outputs/apk/";
    public static final String APK_NAME = "app-appium.apk";
    public static final String PACKAGE_NAME = "com.triangleleft.flashcards";
    public static final String ACTIVITY_NAME = ".ui.login.LoginActivity";

    public static AppiumAndroidDriver create(URL remoteAddress, boolean fullReset) {
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, APK_PATH);
        File appFile = new File(appDir, APK_NAME);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5556");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        if (fullReset) {
            capabilities.setCapability(MobileCapabilityType.APP, appFile.getAbsolutePath());
            capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
        } else {
            capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        }
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, PACKAGE_NAME);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ACTIVITY_NAME);
        return new AppiumAndroidDriver(remoteAddress, capabilities);
    }

    private AppiumAndroidDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }
}
