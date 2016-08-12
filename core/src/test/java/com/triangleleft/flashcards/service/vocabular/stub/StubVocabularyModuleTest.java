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

package com.triangleleft.flashcards.service.vocabular.stub;

import static org.mockito.Mockito.when;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import rx.observers.TestSubscriber;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class StubVocabularyModuleTest {

    private StubVocabularyModule module;

    @Mock
    AccountModule accountModule;
    @Mock
    VocabularyWordsRepository repository;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        module = new StubVocabularyModule(accountModule, repository);
    }

    @Test
    public void loadVocabularyWords() throws Exception {
        UserData data = UserData.create(Collections.emptyList(), "", "", "ui", "learn");
        when(accountModule.getUserData()).thenReturn(Optional.of(data));

        TestSubscriber<List<VocabularyWord>> subscriber = TestSubscriber.create();
        module.loadVocabularyWords().subscribe(subscriber);
        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);

        subscriber.assertNoErrors();
    }

    @Test
    public void refreshVocabularyWords() throws Exception {
        UserData data = UserData.create(Collections.emptyList(), "", "", "ui", "learn");
        when(accountModule.getUserData()).thenReturn(Optional.of(data));

        TestSubscriber<List<VocabularyWord>> subscriber = TestSubscriber.create();
        module.refreshVocabularyWords().subscribe(subscriber);
        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);

        subscriber.assertNoErrors();
    }
}