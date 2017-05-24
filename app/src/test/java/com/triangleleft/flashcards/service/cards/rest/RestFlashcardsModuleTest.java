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

import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class RestFlashcardsModuleTest {

    @Mock
    RestService service;
    @Mock
    AccountModule accountModule;
    @Mock
    VocabularyWordsRepository wordsRepository;
    private RestFlashcardsModule module;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        module = new RestFlashcardsModule(service, accountModule, wordsRepository);
    }

    @Test
    public void getFlashcards() {
//        FlashcardResponseModel model = mock(FlashcardResponseModel.class);
//        FlashcardTestData mockData = FlashcardTestData.create("ui", "learn", Collections.emptyList());
//        when(model.toTestData()).thenReturn(mockData);
//        when(service.getFlashcardData(anyInt(), anyBoolean(), anyLong())).thenReturn(Call.just(model));
//
//        TestAction<FlashcardTestData> action = new TestAction<>();
//        module.getFlashcards().enqueue(action, Actions.empty());
//
//        action.assertValue(mockData);
    }

//    @Test
//    public void postResults() {
//        List<FlashcardWordResult> results = Arrays.asList(
//                FlashcardWordResult.create(FlashcardWord.create("word", "translation", "id"), true),
//                FlashcardWordResult.create(FlashcardWord.create("word2", "translation2", "id2"), false)
//        );
//        FlashcardTestResult result = FlashcardTestResult.create("ui", "learn", results);
//        when(service.postFlashcardResults(any())).thenReturn(Call.just(null));
//
//        module.postResult(result);
//
//        ArgumentCaptor<FlashcardResultsController> captor = ArgumentCaptor
//                .forClass(FlashcardResultsController.class);
//        // Check that post is called
//        verify(service).postFlashcardResults(captor.capture());
//        FlashcardResultsController controller = captor.getValue();
//        // Check that we path language pair
//        assertThat(controller.uiLanguage, equalTo("ui"));
//        assertThat(controller.learningLanguage, equalTo("learn"));
//        // Check that we pass results
//        FlashcardResultsController.FlashcardResultModel flashcardResultModel = controller.flashcardResults.get(0);
//        assertThat(flashcardResultModel.id, equalTo("id"));
//        assertThat(flashcardResultModel.correct, equalTo(1));
//        flashcardResultModel = controller.flashcardResults.get(1);
//        assertThat(flashcardResultModel.id, equalTo("id2"));
//        assertThat(flashcardResultModel.correct, equalTo(0));
//    }
}