package com.triangleleft.flashcards.service.vocabular.rest;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularData;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsCache;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

import static com.annimon.stream.Collectors.joining;
import static com.annimon.stream.Collectors.toList;

@FunctionsAreNonnullByDefault
public class RestVocabularyModule implements VocabularyModule {

    private static final Logger logger = LoggerFactory.getLogger(RestVocabularyModule.class);
    private final IDuolingoRest service;
    private final SettingsModule settingsModule;
    private final VocabularyWordsCache provider;

    @Inject
    public RestVocabularyModule(IDuolingoRest service, SettingsModule settingsModule, VocabularyWordsCache provider) {
        this.service = service;
        this.settingsModule = settingsModule;
        this.provider = provider;
    }

    @Override
    public Observable<List<VocabularyWord>> loadVocabularyWords() {
        logger.debug("loadVocabularyWords()");
        Optional<UserData> userData = settingsModule.getCurrentUserData();
        if (userData.isPresent()) {
            return Observable.concat(getCachedData(userData.get()), refreshVocabularyWords());
        } else {
            return refreshVocabularyWords();
        }

    }

    @Override
    public Observable<List<VocabularyWord>> refreshVocabularyWords() {
        logger.debug("refreshVocabularyWords()");
        return service.getVocabularList(System.currentTimeMillis())
                .map(VocabularResponseModel::toVocabularData)
                .map(VocabularData::getWords)
                .flatMapIterable(list -> list) // split list of item into stream of items
                .buffer(10) // group them by 10
                .flatMap(list -> Observable.just(list) // for each group translate it in parallel
                        .subscribeOn(Schedulers.io())
                        .map(this::translate))
                .flatMapIterable(list -> list) // split all groups into one stream
                .toList() // group them back to one list
                .doOnNext(this::updateCache);
    }

    private List<VocabularyWord> translate(List<VocabularyWord> words) {
        String query = Stream.of(words)
                .map(VocabularyWord::getWord)
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

    private VocabularyWord getTranslation(VocabularyWord word, WordTranslationModel model) {
        List<String> strings = model.get(word.getWord());
        if (strings == null) {
            strings = Collections.emptyList();
        }
        return word.withTranslations(strings);
    }

    private void updateCache(List<VocabularyWord> words) {
        provider.putWords(words);
    }

    private Observable<List<VocabularyWord>> getCachedData(UserData userData) {
        String uiLanguageId = userData.getUiLanguageId();
        String learningLanguageId = userData.getLearningLanguageId();
        return Observable
                .defer(() -> Observable.just(provider.getWords(uiLanguageId, learningLanguageId)))
                .filter(list -> !list.isEmpty());

    }

}
