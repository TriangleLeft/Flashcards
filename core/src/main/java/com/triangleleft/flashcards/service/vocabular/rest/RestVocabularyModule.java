package com.triangleleft.flashcards.service.vocabular.rest;

import com.annimon.stream.Stream;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.TranslationService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyData;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import com.triangleleft.flashcards.service.vocabular.rest.model.VocabularyResponseModel;
import com.triangleleft.flashcards.service.vocabular.rest.model.WordTranslationModel;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.annimon.stream.Collectors.joining;
import static com.annimon.stream.Collectors.toList;

@FunctionsAreNonnullByDefault
public class RestVocabularyModule implements VocabularyModule {

    public static final String MAIN_EXECUTOR = "mainExecutor";
    private static final Logger logger = LoggerFactory.getLogger(RestVocabularyModule.class);
    private final RestService service;
    private final AccountModule accountModule;
    private final VocabularyWordsRepository provider;
    private final TranslationService translationService;
    private final Executor mainExecutor;

    @Inject
    public RestVocabularyModule(RestService service, TranslationService translationService, AccountModule accountModule,
                                VocabularyWordsRepository provider, @Named(MAIN_EXECUTOR) Executor mainExecutor) {
        this.service = service;
        this.translationService = translationService;
        this.accountModule = accountModule;
        this.provider = provider;
        this.mainExecutor = mainExecutor;
    }

    @Override
    public Observable<List<VocabularyWord>> loadVocabularyWords() {
        logger.debug("loadVocabularyWords()");
        return refreshVocabularyWords().publish(network -> Observable.merge(network,
                getCachedData(accountModule.getUserData().get()).takeUntil(network)));

    }

    @Override
    public Observable<List<VocabularyWord>> refreshVocabularyWords() {
        logger.debug("refreshVocabularyWords()");
        return service.getVocabularyList(System.currentTimeMillis())
                .map(VocabularyResponseModel::toVocabularyData)
                .map(VocabularyData::getWords)
                .flatMapIterable(list -> list) // split list of item into stream of items
                .buffer(10) // group them by 10 (to mimic web client)
                .flatMap(list -> Observable.just(list) // for each group translate it in parallel
                        .subscribeOn(Schedulers.io())
                        .map(this::translate))
                .flatMapIterable(list -> list) // split all groups into one stream
                .toList() // group them back to one list
                .toObservable()
                .doOnNext(this::updateCache);
    }

    private List<VocabularyWord> translate(List<VocabularyWord> words) {
        String query = Stream.of(words)
                .map(VocabularyWord::getWord)
                .map(string -> '"' + string + '"')
                .collect(joining(","));
        query = "[" + query + "]";
        WordTranslationModel model = translationService
                .getTranslation(words.get(0).getLearningLanguage(), words.get(0).getUiLanguage(), query)
                .blockingFirst();
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
