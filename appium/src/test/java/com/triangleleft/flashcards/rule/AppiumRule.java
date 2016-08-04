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
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.TimeOutDuration;
import org.junit.rules.TestRule;
import org.openqa.selenium.WebElement;

public interface AppiumRule extends TestRule {

    AppiumDriver<WebElement> getDriver();

    AppiumFieldDecorator getDecorator();

    TimeOutDuration getTimeOutDuration();

    FlashcardsApp getApp();
}
