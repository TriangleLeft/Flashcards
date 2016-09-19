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

package com.triangleleft.flashcards.test;

import com.google.gson.Gson;

import com.triangleleft.flashcards.page.DrawerPage;
import com.triangleleft.flashcards.page.MainPage;
import com.triangleleft.flashcards.rule.AppiumRule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.settings.rest.model.UserDataModel;
import com.triangleleft.flashcards.util.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.Dimension;

import static com.triangleleft.flashcards.util.TestUtils.hasText;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class DrawerTest {

    @Rule
    public AppiumRule appiumRule = new AppiumRule(false);
    private UserData userData;

    @Before
    public void before() {
        // We don't want to duplicate constants is json and here, so read json and use resolved model
        userData = new Gson().fromJson(TestUtils.getReader("userdata/userdata_french.json"), UserDataModel.class)
                .toUserData();
    }

    @Test
    public void drawer() throws InterruptedException {
        MainPage main = appiumRule.getApp().mainPage();
        // Title should show current learning language
        assertThat(main.title, hasText(userData.getCurrentLearningLanguage().get().getName()));

        openDrawer();
        DrawerPage drawer = appiumRule.getApp().drawerPage();
        // Username is shown
        assertThat(drawer.userName, hasText(userData.getUsername()));
        // learning languages are shown
        assertThat(drawer.languages.get(0), hasText(userData.getSortedLanguages().get(0).getName()));
        assertThat(drawer.languages.get(1), hasText(userData.getSortedLanguages().get(1).getName()));

        // Switch language to second one
        drawer.languages.get(1).click();
//        assertThat(drawer.languages.get(0), hasText(SECOND_LANGUAGE));
//        assertThat(drawer.languages.get(1), hasText(FIRST_LANGUAGE));
//
//        // Select first language again
//        drawer.languages.get(1).click();
//        assertThat(drawer.languages.get(0), hasText(FIRST_LANGUAGE));
//        assertThat(main.title, hasText(FIRST_LANGUAGE));
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
