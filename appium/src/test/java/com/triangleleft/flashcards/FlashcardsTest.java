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
import static org.hamcrest.core.Is.is;

import com.triangleleft.flashcards.page.FlashcardPage;
import com.triangleleft.flashcards.page.MainPage;
import com.triangleleft.flashcards.rule.AppiumAndroidRule;
import com.triangleleft.flashcards.rule.AppiumRule;
import org.junit.Rule;
import org.junit.Test;

public class FlashcardsTest {
    public static final int FLASHCARDS_COUNT = 15;
    @Rule
    public AppiumRule appium = new AppiumAndroidRule();

    @Test
    public void flashcardsNoErrors() throws InterruptedException {
        MainPage main = appium.getApp().mainPage();
        main.flashcardsButton.click();

        FlashcardPage flashcard = appium.getApp().flashcardPage();

        // FIXME: our default user has less that 15 knwon words
        // should query card state each time
        while (flashcard.card.isDisplayed()) {
            flashcard.card.click();
            flashcard.buttonRight.click();
            Thread.sleep(3000);
        }

        assertThat(flashcard.resultSuccessText.isDisplayed(), is(true));
    }
}
