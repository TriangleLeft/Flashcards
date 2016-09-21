package com.triangleleft.flashcards.rule;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class AppiumIOSDriver extends IOSDriver<WebElement> {

    public static final String APK_PATH = "../ios/build/Debug-iphonesimulator/";
    public static final String APK_NAME = "Flashcards-Appium.app";

    public static AppiumIOSDriver create(URL remoteAddress, boolean fullReset) {
        File appDir = new File(APK_PATH);
        File appFile = new File(appDir, APK_NAME);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5556");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.3");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 4s");
        //capabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.triangleleft.Flashcards");
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        if (fullReset) {
            capabilities.setCapability(MobileCapabilityType.APP, appFile.getAbsolutePath());
            capabilities.setCapability(MobileCapabilityType.FULL_RESET, true);
        } else {
            capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
        }

        return new AppiumIOSDriver(remoteAddress, capabilities);
    }

    private AppiumIOSDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }
}
