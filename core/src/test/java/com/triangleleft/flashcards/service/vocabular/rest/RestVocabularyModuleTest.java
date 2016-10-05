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
import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.TranslationService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import com.triangleleft.flashcards.service.vocabular.rest.model.VocabularyResponseModel;
import com.triangleleft.flashcards.service.vocabular.rest.model.WordTranslationModel;
import com.triangleleft.flashcards.util.TestObserver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class RestVocabularyModuleTest {

    private static final String UI_LANG = "uiLang";
    private static final String LEARN_LANG = "learnLang";
    private static final String WORD = "word";
    private static final String TRANSLATION = "translation";

    @Mock
    RestService service;
    @Mock
    TranslationService translationService;
    @Mock
    AccountModule accountModule;
    @Mock
    VocabularyWordsRepository cache;
    private RestVocabularyModule module;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        module = new RestVocabularyModule(service, translationService, accountModule, cache, Runnable::run);
        when(accountModule.getUserData()).thenReturn(Optional
                .of(UserData.create(Collections.emptyList(), "", "", UI_LANG, LEARN_LANG)));
    }

    @Test
    public void loadVocabularyWords() {
        VocabularyWord cachedWord = makeWord("cachedWord", TRANSLATION);
        when(cache.getWords(UI_LANG, LEARN_LANG)).thenReturn(Collections.singletonList(cachedWord));
        VocabularyWord liveWord = makeWord(WORD, TRANSLATION);
        addVocabularyData(liveWord);
        addTranslation(WORD, Collections.singletonList(TRANSLATION));

        TestObserver<List<VocabularyWord>> observer = new TestObserver<>();
        module.loadVocabularyWords().enqueue(observer);

        List<VocabularyWord> first = observer.getOnNextEvents().get(0);
        assertThat(first, contains(cachedWord));
        List<VocabularyWord> second = observer.getOnNextEvents().get(1);
        assertThat(second, contains(liveWord));
        verify(cache).putWords(second);
    }

    @Test
    public void loadVocabularyWordsNoTranslations() {
        addVocabularyData(makeWord(WORD, TRANSLATION));
        addTranslation(WORD, null);

        TestObserver<List<VocabularyWord>> observer = new TestObserver<>();
        module.loadVocabularyWords().enqueue(observer);

        VocabularyWord word = observer.getOnNextEvents().get(0).get(0);
        assertThat(word.getTranslations(), equalTo(Collections.emptyList()));
    }

    @Test
    public void loadVocabularyWordsNoCache() {
        when(cache.getWords(UI_LANG, LEARN_LANG)).thenReturn(Collections.emptyList());
        VocabularyWord liveWord = makeWord(WORD, TRANSLATION);
        addVocabularyData(liveWord);
        addTranslation(WORD, Collections.singletonList(TRANSLATION));

        TestObserver<List<VocabularyWord>> observer = new TestObserver<>();
        module.loadVocabularyWords().enqueue(observer);

        List<VocabularyWord> live = observer.getOnNextEvents().get(0);
        assertThat(live, contains(liveWord));
    }

    @Test
    public void refreshVocabularyWords() {
        addVocabularyData(makeWord(WORD, TRANSLATION));
        addTranslation(WORD, Collections.singletonList(TRANSLATION));

        TestObserver<List<VocabularyWord>> observer = new TestObserver<>();
        module.loadVocabularyWords().enqueue(observer);

        List<VocabularyWord> result = observer.getOnNextEvents().get(0);
        VocabularyWord word = result.get(0);
        assertThat(word.getWord(), equalTo(WORD));
        assertThat(word.getTranslations(), contains(TRANSLATION));
        verify(cache).putWords(result);
    }

    private void addTranslation(String word, List<String> translations) {
        WordTranslationModel model = new WordTranslationModel();
        model.put(word, translations);
        when(translationService.getTranslation(LEARN_LANG, UI_LANG, "[\"" + word + "\"]"))
                .thenReturn(Call.just(model));
    }

    private VocabularyWord makeWord(String word, String translation) {
        return VocabularyWord
                .create(word, word, Optional.empty(), Optional.empty(), 0, Collections.singletonList(translation),
                        UI_LANG, LEARN_LANG);
    }

    private void addVocabularyData(VocabularyWord word) {
        List<VocabularyWord> words = Collections.singletonList(word);
        VocabularyData data = VocabularyData.create(words, UI_LANG, LEARN_LANG);
        VocabularyResponseModel model = Mockito.mock(VocabularyResponseModel.class);
        when(model.toVocabularyData()).thenReturn(data);
        when(service.getVocabularyList(Mockito.anyLong())).thenReturn(Call.just(model));
    }
}