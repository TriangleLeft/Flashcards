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

import com.triangleleft.flashcards.page.DrawerPage;
import com.triangleleft.flashcards.page.MainPage;
import com.triangleleft.flashcards.rule.AppiumRule;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.TranslationService;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.settings.rest.model.UserDataModel;
import com.triangleleft.flashcards.util.MockServerResponse;
import com.triangleleft.flashcards.util.ResourcesUtils;
import com.triangleleft.flashcards.util.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.triangleleft.flashcards.util.TestUtils.hasText;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class DrawerTest {

    @Rule
    public AppiumRule appium = new AppiumRule(true);

    @Before
    public void before() {
        TestUtils.loginWithUserdata(appium, ResourcesUtils.USERDATA_SPANISH);
        appium.enqueue(RestService.PATH_VOCABULARY, ResourcesUtils.VOCABULARY_SPANISH);
        appium.enqueue(TranslationService.PATH_TRANSLATION, ResourcesUtils.VOCABULARY_SPANISH_TRANSLATION);
    }

    @Test
    public void languageChange() throws InterruptedException {
        UserData userData = ResourcesUtils.getModel(ResourcesUtils.USERDATA_SPANISH, UserDataModel.class).toUserData();
        MainPage mainPage = appium.getApp().mainPage();
        mainPage.openDrawer();
        DrawerPage drawer = appium.getApp().drawerPage();
        // Username is shown
        assertThat(drawer.userName, hasText(userData.getUsername()));
        // current learning should be first language
        assertThat(drawer.languages.get(0), hasText(userData.getLanguages().get(1).getName()));
        // Then all other languages
        assertThat(drawer.languages.get(1), hasText(userData.getLanguages().get(0).getName()));

        // Switch language to french
        appium.enqueue(RestService.PATH_SWITCH_LANGUAGE, MockServerResponse.make(ResourcesUtils.LANGUAGE_FRENCH));
        drawer.languages.get(1).click();

        // Selected language should become first
        assertThat(drawer.languages.get(0), hasText(userData.getLanguages().get(0).getName()));
    }
}
