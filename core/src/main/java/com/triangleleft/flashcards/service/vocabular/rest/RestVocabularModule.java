package com.triangleleft.flashcards.service.vocabular.rest;

import com.annimon.stream.Stream;
import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.common.AbstractProvider;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.VocabularData;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;
import com.triangleleft.flashcards.service.vocabular.VocabularWordsCache;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static com.annimon.stream.Collectors.joining;
import static com.annimon.stream.Collectors.toList;

@FunctionsAreNonnullByDefault
public class RestVocabularModule extends AbstractProvider implements IVocabularModule {

    private static final Logger logger = LoggerFactory.getLogger(RestVocabularModule.class);
    private final IDuolingoRest service;
    private final SettingsModule settingsModule;
    private final VocabularWordsCache provider;

    @Inject
    public RestVocabularModule(IDuolingoRest service, SettingsModule settingsModule, VocabularWordsCache provider) {
        this.service = service;
        this.settingsModule = settingsModule;
        this.provider = provider;
    }

    @Override
    public Observable<List<VocabularWord>> getVocabularWords(boolean refresh) {
        logger.debug("getVocabularList() called");
        Observable<List<VocabularWord>> observable = service.getVocabularList(System.currentTimeMillis())
                .map(VocabularResponseModel::toVocabularData)
                .map(VocabularData::getWords)
                .flatMapIterable(list -> list)
                .buffer(10)
                .map(this::translate)
                .flatMapIterable(list -> list)
                .toList()
                .doOnNext(this::updateCache);
        // For fresh calls, try to return db cache
        if (!refresh) {
            observable = observable.startWith(getCachedData());
        }
        return observable;
    }

    private List<VocabularWord> translate(List<VocabularWord> words) {
        String query = Stream.of(words)
                .map(VocabularWord::getWord)
                .map(string -> '"' + string + '"')
                .collect(joining(","));
        query = "[" + query + "]";
        WordTranslationModel model =
                service.getTranslation(words.get(0).getLearningLanguage(), words.get(0).getUiLanguage(), query)
                        .toBlocking().first();
        return Stream.of(words)
                .map(word -> getTranslation(word, model))
                .collect(toList());
    }

    private VocabularWord getTranslation(VocabularWord word, WordTranslationModel model) {
        List<String> strings = model.get(word.getWord());
        if (strings == null) {
            strings = Collections.emptyList();
        }
        return word.withTranslations(strings);
    }


    private void updateCache(List<VocabularWord> words) {
        provider.putWords(words);
    }

    @Nullable
    private List<VocabularWord> getCachedData() {
        UserData userData = settingsModule.getCurrentUserData();
        return provider.getWords(userData.getUiLanguageId(), userData.getLearningLanguageId());
    }

}
