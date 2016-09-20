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

package com.triangleleft.flashcards.page;

import org.openqa.selenium.WebElement;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class MainPage extends BasePage {

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/action_settings")
    public WebElement settingsButton;
    @AndroidFindBy(id = "com.triangleleft.flashcards:id/button_flashcards")
    public WebElement flashcardsButton;
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@resource-id='com.triangleleft.flashcards:id/toolbar']/android.widget.TextView[1]")
    public WebElement title;
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@resource-id='com.triangleleft.flashcards:id/toolbar']/android.widget.ImageButton[1]")
    public WebElement drawerButton;

    public MainPage(AppiumFieldDecorator driver) {
        super(driver);
    }

    public void openDrawer() {
        drawerButton.click();
    }
}
