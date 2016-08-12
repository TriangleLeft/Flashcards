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

package com.triangleleft.flashcards.service.cards.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardWord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

@RunWith(JUnit4.class)
public class FlashcardResponseModelTest {

    private FlashcardResponseModel model;

    @Before
    public void before() {
        model = new FlashcardResponseModel();
    }

    @Test
    public void toTestData() {
        FlashcardResponseModel.FlashcardModel flashcard = new FlashcardResponseModel.FlashcardModel();
        flashcard.id = "id";
        flashcard.word = "word";
        flashcard.translation = "translation";
        model.flashcardData = Collections.singletonList(flashcard);
        model.uiLanguage = "ui";
        model.learningLanguage = "learn";

        FlashcardTestData testData = model.toTestData();

        assertThat(testData.getUiLanguage(), equalTo("ui"));
        assertThat(testData.getLearningLanguage(), equalTo("learn"));
        FlashcardWord word = testData.getWords().get(0);
        assertThat(word.getWord(), equalTo("word"));
        assertThat(word.getTranslation(), equalTo("translation"));
        assertThat(word.getId(), equalTo("id"));
    }
}