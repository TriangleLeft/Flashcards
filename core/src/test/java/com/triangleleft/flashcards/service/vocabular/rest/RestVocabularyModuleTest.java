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

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.Mockito.*;

//import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

@Ignore
@RunWith(JUnit4.class)
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
        MockitoAnnotations.initMocks(this);
        // module = new RestVocabularyModule(service, accountModule, cache);
        when(accountModule.getUserData()).thenReturn(Optional
            .of(UserData.create(Collections.emptyList(), "", "", UI_LANG, LEARN_LANG)));
    }

//    @Test
//    public void loadVocabularyWords() {
//        VocabularyWord cachedWord = makeWord("cachedWord", TRANSLATION);
//        when(cache.getWords(UI_LANG, LEARN_LANG)).thenReturn(Collections.singletonList(cachedWord));
//        VocabularyWord liveWord = makeWord(WORD, TRANSLATION);
//        addVocabularyData(liveWord);
//        addTranslation(WORD, Collections.singletonList(TRANSLATION));
//
//        TestSubscriber<List<VocabularyWord>> subscriber = loadWords();
//
//        List<VocabularyWord> cache = subscriber.getOnNextEvents().get(0);
//        assertThat(cache, containsInAnyOrder(cachedWord));
//        List<VocabularyWord> live = subscriber.getOnNextEvents().get(1);
//        assertThat(live, containsInAnyOrder(liveWord));
//    }

//    @Test
//    public void loadVocabularyWordsNoTranslations() {
//        addVocabularyData(makeWord(WORD, TRANSLATION));
//        addTranslation(WORD, null);
//
//        TestSubscriber<List<VocabularyWord>> subscriber = loadWords();
//
//        VocabularyWord word = subscriber.getOnNextEvents().get(0).get(0);
//        assertThat(word.getTranslations(), equalTo(Collections.emptyList()));
//    }

//    @Test
//    public void loadVocabularyWordsNoCache() {
//        when(cache.getWords(UI_LANG, LEARN_LANG)).thenReturn(Collections.emptyList());
//        VocabularyWord liveWord = makeWord(WORD, TRANSLATION);
//        addVocabularyData(liveWord);
//        addTranslation(WORD, Collections.singletonList(TRANSLATION));
//
//        TestSubscriber<List<VocabularyWord>> subscriber = loadWords();
//
//        subscriber.assertValueCount(1);
//        List<VocabularyWord> live = subscriber.getOnNextEvents().get(0);
//        assertThat(live, containsInAnyOrder(liveWord));
//    }

//    @Test
//    public void refreshVocabularyWords() {
//        addVocabularyData(makeWord(WORD, TRANSLATION));
//        addTranslation(WORD, Collections.singletonList(TRANSLATION));
//
//        TestSubscriber<List<VocabularyWord>> subscriber = TestSubscriber.create();
//        module.refreshVocabularyWords().subscribe(subscriber);
//        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);
//
//        List<VocabularyWord> result = subscriber.getOnNextEvents().get(0);
//        VocabularyWord word = result.get(0);
//        assertThat(word.getWord(), equalTo(WORD));
//        assertThat(word.getTranslations(), containsInAnyOrder(TRANSLATION));
//
//        verify(cache).putWords(result);
//    }
//
//    private TestSubscriber<List<VocabularyWord>> loadWords() {
//        TestSubscriber<List<VocabularyWord>> subscriber = TestSubscriber.create();
//        module.loadVocabularyWords().subscribe(subscriber);
//        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);
//        return subscriber;
//    }
//
//    private void addTranslation(String word, List<String> translations) {
//        WordTranslationModel model = new WordTranslationModel();
//        model.put(word, translations);
//        when(service.getTranslation(LEARN_LANG, UI_LANG, "[\"" + word + "\"]")).thenReturn(Observable.just(model));
//    }
//
//    private VocabularyWord makeWord(String word, String translation) {
//        return VocabularyWord
//            .create(word, word, Optional.empty(), Optional.empty(), 0, Collections.singletonList(translation), UI_LANG, LEARN_LANG);
//    }
//
//    private void addVocabularyData(VocabularyWord word) {
//        List<VocabularyWord> words = Collections.singletonList(word);
//        VocabularyData data = VocabularyData.create(words, UI_LANG, LEARN_LANG);
//        VocabularyResponseModel model = mock(VocabularyResponseModel.class);
//        when(model.toVocabularyData()).thenReturn(data);
//        when(service.getVocabularyList(anyLong())).thenReturn(Observable.just(model));
//    }
}