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
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.TranslationService;
import com.triangleleft.flashcards.service.vocabular.VocabularyData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.service.vocabular.rest.model.VocabularyResponseModel;
import com.triangleleft.flashcards.service.vocabular.rest.model.WordTranslationModel;
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
public class VocabularyTest {

    @Rule
    public AppiumRule appium = new AppiumRule(true);
    private VocabularyData vocabList;
    private WordTranslationModel vocabListTranslation;

    @Before
    public void before() {
        appium.enqueue(RestService.PATH_VOCABULARY, ResourcesUtils.VOCABULARY_SPANISH);
        appium.enqueue(TranslationService.PATH_TRANSLATION, ResourcesUtils.VOCABULARY_SPANISH_TRANSLATION);
        TestUtils.loginWithUserdata(appium, ResourcesUtils.USERDATA_SPANISH);

        vocabList = ResourcesUtils.getModel(ResourcesUtils.VOCABULARY_SPANISH, VocabularyResponseModel.class)
                .toVocabularyData();
        vocabListTranslation = ResourcesUtils.getModel(ResourcesUtils.VOCABULARY_SPANISH_TRANSLATION,
                WordTranslationModel.class);
    }

    @Test
    public void fullWordInfoIsShown() throws InterruptedException {
        VocabularyListPage listPage = appium.getApp().vocabularyListPage();
        // Word with full info available
        VocabularyWord word = vocabList.getWords().get(1);
        listPage.clickOn(word.getWord());

        VocabularyWordPage wordPage = appium.getApp().vocabularyWordPage();
        assertThat(wordPage.title, hasText(word.getWord()));
        assertThat(wordPage.gender, hasText(word.getGender().get()));
        assertThat(wordPage.pos, hasText(word.getPos().get()));
        assertThat(wordPage.translation, hasText(vocabListTranslation.get(word.getWord()).get(0)));
    }

    @Test
    public void missingWordInfoIsShown() {
        VocabularyListPage listPage = appium.getApp().vocabularyListPage();
        // Word without any info available
        VocabularyWord word = vocabList.getWords().get(2);
        listPage.clickOn(word.getWord());

        VocabularyWordPage wordPage = appium.getApp().vocabularyWordPage();
        assertThat(wordPage.title, hasText(word.getWord()));
        assertThat(wordPage.gender, hasText("N/A"));
        assertThat(wordPage.pos, hasText("N/A"));
        assertThat(wordPage.translation, hasText("N/A"));
    }

}
