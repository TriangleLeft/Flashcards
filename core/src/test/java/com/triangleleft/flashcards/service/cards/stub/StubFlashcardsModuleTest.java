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

package com.triangleleft.flashcards.service.cards.stub;

import static org.mockito.Mockito.mock;

import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardTestResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import rx.observers.TestSubscriber;

import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class StubFlashcardsModuleTest {

    private StubFlashcardsModule module;

    @Before
    public void before() {
        module = new StubFlashcardsModule();
    }

    @Test
    public void getFlashcards() throws Exception {
        TestSubscriber<FlashcardTestData> subscriber = TestSubscriber.create();
        module.getFlashcards().subscribe(subscriber);
        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);

        subscriber.assertValueCount(1);
    }

    @Test
    public void postResult() throws Exception {
        module.postResult(mock(FlashcardTestResult.class));
    }
}