package com.triangleleft.flashcards.page;

import org.openqa.selenium.WebElement;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;

public class LoginPage extends BasePage {

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/login_email")
    @iOSFindBy(xpath = "//UIAApplication[1]/UIAWindow[1]/UIATextField[1]")
    public MobileElement emailText;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/login_password")
    @iOSFindBy(xpath = "//UIAApplication[1]/UIAWindow[1]/UIATextField[2]")
    public MobileElement passwordText;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/login_switch")
    @iOSFindBy(xpath = "//UIAApplication[1]/UIAWindow[1]/UIASwitch[1]")
    public WebElement rememberSwitch;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/login_button")
    @iOSFindBy(xpath = "//UIAApplication[1]/UIAWindow[1]/UIAButton[1]")
    public WebElement buttonSignIn;

    @AndroidFindBy(xpath = "//TextInputLayout[@resource-id='com.triangleleft.flashcards:id/login_email_layout']/android.widget.LinearLayout/android.widget.TextView")
    @iOSFindBy(xpath = "//UIAApplication[1]/UIAWindow[1]/UIAStaticText[2]")
    public WebElement loginError;

    @AndroidFindBy(xpath = "//TextInputLayout[@resource-id='com.triangleleft.flashcards:id/login_password_layout']/android.widget.LinearLayout/android.widget.TextView")
    @iOSFindBy(xpath = "//UIAApplication[1]/UIAWindow[1]/UIAStaticText[3]")
    public WebElement passwordError;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/login_progress")
    @iOSFindBy(xpath = "//UIAApplication[1]/UIAWindow[1]/UIAActivityIndicator[1]")
    public WebElement progressBar;

    public LoginPage(AppiumFieldDecorator driver) {
        super(driver);
    }

    public void setLogin(String login) {
        emailText.click();
        emailText.sendKeys(login);
    }

    public void setPassword(String password) {
        passwordText.click();
        passwordText.sendKeys(password);
    }

    public void setRememberSwitch(boolean remember) {
        if (rememberSwitch.isSelected() != remember) {
            rememberSwitch.click();
        }
    }

    public void login() {
        buttonSignIn.click();
    }
}
