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

import com.triangleleft.flashcards.page.FlashcardPage;
import com.triangleleft.flashcards.page.MainPage;
import com.triangleleft.flashcards.rule.AppiumRule;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.service.cards.rest.FlashcardResponseModel;
import com.triangleleft.flashcards.util.MockJsonResponse;
import com.triangleleft.flashcards.util.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.triangleleft.flashcards.util.TestUtils.hasText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FlashcardsTest {

    @Rule
    public AppiumRule appium = new AppiumRule(true);

    @Before
    public void before() {
        TestUtils.loginWithUserdata(appium, MockJsonResponse.USERDATA_SPANISH);
    }

    @Test
    public void flashcardIsShown() {
        appium.enqueue(RestService.PATH_FLASHCARDS, MockJsonResponse.FLASHCARDS_SPANISH_SINGLE);
        FlashcardTestData flashcardsData =
                MockJsonResponse.getModel(MockJsonResponse.FLASHCARDS_SPANISH_SINGLE, FlashcardResponseModel.class)
                        .toTestData();

        MainPage main = appium.getApp().mainPage();
        main.flashcardsButton.click();

        FlashcardPage flashcardPage = appium.getApp().flashcardPage();
        FlashcardWord flashcardWord = flashcardsData.getWords().get(0);
        assertThat(flashcardPage.word, hasText(flashcardWord.getWord()));
        flashcardPage.word.click();

        assertThat(flashcardPage.translation, hasText(flashcardWord.getTranslation()));
    }

    @Test
    public void passNoErrors() throws InterruptedException {
        appium.enqueue(RestService.PATH_FLASHCARDS, MockJsonResponse.FLASHCARDS_SPANISH);
        FlashcardTestData flashcardsData =
                MockJsonResponse.getModel(MockJsonResponse.FLASHCARDS_SPANISH, FlashcardResponseModel.class)
                        .toTestData();

        MainPage main = appium.getApp().mainPage();
        main.flashcardsButton.click();

        FlashcardPage flashcardPage = appium.getApp().flashcardPage();
        for (int i = 0; i < flashcardsData.getWords().size(); i++) {
            flashcardPage.showAnswerButton.click();
            flashcardPage.buttonRight.click();
            Thread.sleep(1000);
        }

        assertThat(flashcardPage.resultSuccessText.isDisplayed(), is(true));
    }

    @Test
    public void passErrors() throws InterruptedException {
        appium.enqueue(RestService.PATH_FLASHCARDS, MockJsonResponse.FLASHCARDS_SPANISH);
        appium.enqueue(RestService.PATH_FLASHCARDS, MockJsonResponse.EMPTY);
        FlashcardTestData flashcardsData =
                MockJsonResponse.getModel(MockJsonResponse.FLASHCARDS_SPANISH, FlashcardResponseModel.class)
                        .toTestData();

        MainPage main = appium.getApp().mainPage();
        main.flashcardsButton.click();

        FlashcardPage flashcardPage = appium.getApp().flashcardPage();
        for (int i = 0; i < flashcardsData.getWords().size(); i++) {
            flashcardPage.showAnswerButton.click();
            // Mark second word as error
            if (i == 1) {
                flashcardPage.buttonWrong.click();
            } else {
                flashcardPage.buttonRight.click();
            }
            Thread.sleep(1000);
        }

        assertThat(flashcardPage.resultErrorsText.isDisplayed(), is(true));
        // Check that wrong word is shown
        FlashcardWord wrongWord = flashcardsData.getWords().get(1);
        assertThat(flashcardPage.wrongWords.get(0), hasText(wrongWord.getWord()));
        assertThat(flashcardPage.wrongWordTranslations.get(0), hasText(wrongWord.getTranslation()));
    }

}
