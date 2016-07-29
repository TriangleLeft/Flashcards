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

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DrawerPage extends BasePage {

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/drawer_user_name")
    public WebElement userName;
    @AndroidFindBy(id = "com.triangleleft.flashcards:id/drawer_item_name")
    public List<WebElement> languages;

    public DrawerPage(AppiumFieldDecorator driver) {
        super(driver);
    }
}
