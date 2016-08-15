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

package com.triangleleft.flashcards.service.vocabular.rest.model;

import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.vocabular.VocabularyData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;

@RunWith(JUnit4.class)
public class VocabularyResponseModelTest {

    private VocabularyResponseModel model;

    @Before
    public void before() {
        model = new VocabularyResponseModel();
    }

    @Test
    public void emptyTest() {
        int i = 2 + 3;
    }

//    @Test
//    public void toVocabularyData() throws Exception {
//        VocabularyResponseModel.VocabularyResponseWordModel wordModel
//            = new VocabularyResponseModel.VocabularyResponseWordModel();
//        wordModel.gender = "gender";
//        wordModel.id = "id";
//        wordModel.normalizedString = "norm";
//        wordModel.pos = "pos";
//        wordModel.strengthBars = 3;
//        wordModel.wordString = "word";
//        model.wordList = Collections.singletonList(wordModel);
//        model.fromLanguage = "from";
//        model.learningLanguage = "learn";
//
//        VocabularyData data = model.toVocabularyData();
//
//        assertThat(data.getLearningLanguageId(), equalTo("learn"));
//        assertThat(data.getUiLanguageId(), equalTo("from"));
//        VocabularyWord word = VocabularyWord
//            .create(
//                "word",
//                "norm",
//                Optional.of("pos"),
//                Optional.of("gender"),
//                3,
//                Collections.emptyList(),
//                "from",
//                "learn");
//        assertThat(data.getWords(), containsInAnyOrder(word));
//    }
}