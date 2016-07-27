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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import com.triangleleft.flashcards.page.MainPage;
import com.triangleleft.flashcards.rule.AppiumAndroidRule;
import com.triangleleft.flashcards.rule.AppiumRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.Dimension;

@RunWith(JUnit4.class)
public class PhoneMainTest {

    @Rule
    public AppiumRule appiumRule = new AppiumAndroidRule();

    @Test
    public void test() throws InterruptedException {
        MainPage main = appiumRule.getApp().mainPage();

        assertThat(main.title.getText(), equalTo("Spanish"));

        Dimension size = appiumRule.getDriver().manage().window().getSize();
        appiumRule.getDriver().swipe(
            5,
            size.getHeight() / 2,
            (int) (size.getWidth() * 0.8f),
            size.getHeight() / 2,
            200
        );

        Thread.sleep(5000);
    }
}
