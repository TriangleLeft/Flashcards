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

package com.triangleleft.flashcards.service.vocabular.rest;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RestVocabularyModuleTest {

    private static final String UI_LANG = "uiLang";
    private static final String LEARN_LANG = "learnLang";
    private static final String WORD = "word";
    private static final String TRANSLATION = "translation";
    @Mock
    RestService service;
    @Mock
    AccountModule accountModule;
    @Mock
    VocabularyWordsRepository cache;
    private RestVocabularyModule module;

    @Before
    public void before() {
        module = new RestVocabularyModule(service, accountModule, cache);
        when(accountModule.getUserData()).thenReturn(Optional
            .of(UserData.create(Collections.emptyList(), Optional.empty(), Optional.empty(), UI_LANG, LEARN_LANG)));
    }

    @Test
    public void loadVocabularyWords() {
        VocabularyWord cachedWord = makeWord("cachedWord", TRANSLATION);
        when(cache.getWords(UI_LANG, LEARN_LANG))
            .thenReturn(Collections.singletonList(cachedWord));
        VocabularyWord liveWord = makeWord(WORD, TRANSLATION);
        addVocabularyData(liveWord);
        addTranslation(WORD, TRANSLATION);

        module.loadVocabularyWords();

        TestSubscriber<List<VocabularyWord>> subscriber = TestSubscriber.create();
        module.loadVocabularyWords()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        List<VocabularyWord> cache = subscriber.getOnNextEvents().get(0);
        assertThat(cache, hasItems(cachedWord));
        List<VocabularyWord> live = subscriber.getOnNextEvents().get(1);
        assertThat(live, hasItems(liveWord));
    }

    @Test
    public void refreshVocabularyWords() {
        addVocabularyData(makeWord(WORD, TRANSLATION));
        addTranslation(WORD, TRANSLATION);

        TestSubscriber<List<VocabularyWord>> subscriber = TestSubscriber.create();
        module.refreshVocabularyWords()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        List<VocabularyWord> result = subscriber.getOnNextEvents().get(0);
        VocabularyWord word = result.get(0);
        assertThat(word.getWord(), equalTo(WORD));
        assertThat(word.getTranslations(), hasItems(TRANSLATION));

        verify(cache).putWords(result);
    }

    private void addTranslation(String word, String translation) {
        WordTranslationModel model = new WordTranslationModel();
        model.put(word, Collections.singletonList(translation));
        when(service.getTranslation(LEARN_LANG, UI_LANG, "[\"" + word + "\"]")).thenReturn(Observable.just(model));
    }

    private VocabularyWord makeWord(String word, String translation) {
        return VocabularyWord
            .create(word, word, null, null, 0, Collections.singletonList(translation), UI_LANG, LEARN_LANG);
    }

    private void addVocabularyData(VocabularyWord word) {
        List<VocabularyWord> words = Collections.singletonList(word);
        VocabularyData data = VocabularyData.create(words, UI_LANG, LEARN_LANG);
        VocabularyResponseModel model = mock(VocabularyResponseModel.class);
        when(model.toVocabularData()).thenReturn(data);
        when(service.getVocabularList(anyLong())).thenReturn(Observable.just(model));
    }
}