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

import static com.triangleleft.flashcards.TestUtils.hasText;
import static org.hamcrest.MatcherAssert.assertThat;

import com.triangleleft.flashcards.page.DrawerPage;
import com.triangleleft.flashcards.page.MainPage;
import com.triangleleft.flashcards.rule.AppiumAndroidRule;
import com.triangleleft.flashcards.rule.AppiumRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.Dimension;

@RunWith(JUnit4.class)
public class DrawerTest {

    public static final String FIRST_LANGUAGE = "Spanish";
    public static final String SECOND_LANGUAGE = "French";

    @Rule
    public AppiumRule appiumRule = new AppiumAndroidRule();

    @Test
    public void drawer() throws InterruptedException {
        MainPage main = appiumRule.getApp().mainPage();
        assertThat(main.title, hasText(FIRST_LANGUAGE));

        openDrawer();
        DrawerPage drawer = appiumRule.getApp().drawerPage();
        assertThat(drawer.userName, hasText(LoginTest.LOGIN));
        assertThat(drawer.languages.get(0), hasText(FIRST_LANGUAGE));
        assertThat(drawer.languages.get(1), hasText(SECOND_LANGUAGE));

        // Select second language
        drawer.languages.get(1).click();
        assertThat(drawer.languages.get(0), hasText(SECOND_LANGUAGE));
        assertThat(drawer.languages.get(1), hasText(FIRST_LANGUAGE));

        // Select first language again
        drawer.languages.get(1).click();
        assertThat(drawer.languages.get(0), hasText(FIRST_LANGUAGE));
        assertThat(main.title, hasText(FIRST_LANGUAGE));
    }

    private void openDrawer() {
        Dimension size = appiumRule.getDriver().manage().window().getSize();
        appiumRule.getDriver().swipe(
            5,
            size.getHeight() / 2,
            (int) (size.getWidth() * 0.8f),
            size.getHeight() / 2,
            200
        );
    }
}
