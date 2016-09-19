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

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.triangleleft.flashcards.util.TestUtils.hasText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FlashcardsTest {
    private static final int FLASHCARDS_COUNT = 15;
    private static final int WRONG_WORDS_COUNT = 5;
    @Rule
    public AppiumRule appium = new AppiumRule(false);

    @Test
    public void flashcardsNoErrors() throws InterruptedException {
        MainPage main = appium.getApp().mainPage();
        main.flashcardsButton.click();

        FlashcardPage flashcard = appium.getApp().flashcardPage();

        for (int i = 0; i < FLASHCARDS_COUNT; i++) {
            flashcard.card.click();
            flashcard.buttonRight.click();
            Thread.sleep(3000);
        }

        assertThat(flashcard.resultSuccessText.isDisplayed(), is(true));
    }

    @Test
    public void flashcardsErrors() throws InterruptedException {
        MainPage main = appium.getApp().mainPage();
        main.flashcardsButton.click();

        FlashcardPage flashcard = appium.getApp().flashcardPage();
        List<String> wrongWords = new ArrayList<>();
        List<String> wrongWordTranslations = new ArrayList<>();
        // We have to make sure we would get exact count of wrong words
        Set<Integer> wrongIndexes = new HashSet<>(WRONG_WORDS_COUNT);
        Random random = new Random();
        while (wrongIndexes.size() < WRONG_WORDS_COUNT) {
            while (!wrongIndexes.add(random.nextInt(FLASHCARDS_COUNT)))
                ;
        }

        for (int i = 0; i < FLASHCARDS_COUNT; i++) {
            if (wrongIndexes.contains(i)) {
                // Remember what the word was
                wrongWords.add(flashcard.word.getText());
                flashcard.card.click();
                wrongWordTranslations.add(flashcard.translation.getText());
                flashcard.buttonWrong.click();
            } else {
                flashcard.card.click();
                flashcard.buttonRight.click();
            }
            Thread.sleep(3000);
        }

        assertThat(flashcard.resultErrorsText.isDisplayed(), is(true));
        for (int i = 0; i < WRONG_WORDS_COUNT; i++) {
            assertThat(flashcard.wrongWords.get(i), hasText(wrongWords.get(i)));
            assertThat(flashcard.wrongWordTranslations.get(0), hasText(wrongWordTranslations.get(0)));
        }
    }
}
