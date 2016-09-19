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

import com.triangleleft.flashcards.page.VocabularyListPage;
import com.triangleleft.flashcards.page.VocabularyWordPage;
import com.triangleleft.flashcards.rule.AppiumRule;
import com.triangleleft.flashcards.util.MockServerResponse;
import com.triangleleft.flashcards.util.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.triangleleft.flashcards.util.TestUtils.hasText;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class VocabularyTest {

    @Rule
    public AppiumRule appium = new AppiumRule(true);

    @Before
    public void before() {
        TestUtils.loginWithUserdata(appium, "userdata/userdata_spanish.json");
        appium.enqueue(MockServerResponse.make("userdata/userdata_spanish.json"));
        appium.enqueue(MockServerResponse.make("vocabulary/spanish.json"));
        appium.enqueue(MockServerResponse.make("vocabulary/spanish_translation.json"));
    }

    @Test
    public void fullWordInfoIsShown() throws InterruptedException {
        VocabularyListPage listPage = appium.getApp().vocabularyListPage();
        listPage.clickOn("word2");

        VocabularyWordPage wordPage = appium.getApp().vocabularyWordPage();
        assertThat(wordPage.title, hasText("word2"));
        assertThat(wordPage.gender, hasText("gender2"));
        assertThat(wordPage.pos, hasText("pos2"));
        assertThat(wordPage.translation, hasText("translation2"));
    }

    @Test
    public void missingWordInfoIsShown() {
        VocabularyListPage listPage = appium.getApp().vocabularyListPage();
        listPage.clickOn("word3");

        VocabularyWordPage wordPage = appium.getApp().vocabularyWordPage();
        assertThat(wordPage.title, hasText("word3"));
        assertThat(wordPage.gender, hasText("N/A"));
        assertThat(wordPage.pos, hasText("N/A"));
        assertThat(wordPage.translation, hasText("N/A"));
    }

}
