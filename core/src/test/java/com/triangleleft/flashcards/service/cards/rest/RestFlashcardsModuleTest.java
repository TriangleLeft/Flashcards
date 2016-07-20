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
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardTestResult;
import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.service.cards.FlashcardWordResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class RestFlashcardsModuleTest {

    @Mock
    RestService service;
    private RestFlashcardsModule module;

    @Before
    public void before() {
        module = new RestFlashcardsModule(service);
    }

    @Test
    public void getFlashcards() {
        FlashcardResponseModel model = mock(FlashcardResponseModel.class);
        FlashcardTestData mockData = FlashcardTestData.create("ui", "learn", Collections.emptyList());
        when(model.toTestData()).thenReturn(mockData);
        when(service.getFlashcardData(anyInt(), anyBoolean(), anyLong())).thenReturn(Observable.just(model));

        TestSubscriber<FlashcardTestData> subscriber = TestSubscriber.create();
        module.getFlashcards().subscribe(subscriber);

        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);
        subscriber.assertValue(mockData);
    }

    @Test
    public void postResults() {
        FlashcardWord word = FlashcardWord.create("word", "translation", "id");
        FlashcardWordResult wordResult = FlashcardWordResult.create(word, true);
        FlashcardTestResult result = FlashcardTestResult.create("ui", "learn", Collections.singletonList(wordResult));
        when(service.postFlashcardResults(any())).thenReturn(Observable.empty());

        module.postResult(result);

        ArgumentCaptor<FlashcardResultsController> captor = ArgumentCaptor
            .forClass(FlashcardResultsController.class);
        verify(service).postFlashcardResults(captor.capture());
        FlashcardResultsController controller = captor.getValue();
        assertThat(controller.uiLanguage, equalTo("ui"));
        assertThat(controller.learningLanguage, equalTo("learn"));
        FlashcardResultsController.FlashcardResultModel flashcardResultModel = controller.flashcardResults.get(0);
        assertThat(flashcardResultModel.id, equalTo("id"));
        assertThat(flashcardResultModel.correct, equalTo(1));
    }
}