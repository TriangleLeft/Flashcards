package com.triangleleft.flashcards.service.vocabular.rest;

import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.TranslationService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import com.triangleleft.flashcards.service.vocabular.rest.model.VocabularyResponseModel;
import com.triangleleft.flashcards.service.vocabular.rest.model.WordTranslationModel;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.SafeCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import retrofit2.Call;

@FunctionsAreNonnullByDefault
public class RestVocabularyModule implements VocabularyModule {

    private static final Logger logger = LoggerFactory.getLogger(RestVocabularyModule.class);
    private final RestService service;
    private final AccountModule accountModule;
    private final VocabularyWordsRepository provider;
    private final TranslationService translationService;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Inject
    public RestVocabularyModule(RestService service, TranslationService translationService, AccountModule accountModule,
                                VocabularyWordsRepository provider) {
        this.service = service;
        this.translationService = translationService;
        this.accountModule = accountModule;
        this.provider = provider;
    }

    @Override
    public void loadVocabularyWords(Observer<List<VocabularyWord>> observer) {
        logger.debug("loadVocabularyWords()");
        executor.execute(new GetCachedDataTask(observer));
        executor.execute(new LoadWordsTask(observer));
    }

    @Override
    public void refreshVocabularyWords(Observer<List<VocabularyWord>> observer) {
        logger.debug("refreshVocabularyWords()");
        executor.execute(new LoadWordsTask(observer));
    }

//    private List<VocabularyWord> translate(List<VocabularyWord> words) {
//        String query = Stream.of(words)
//                .map(VocabularyWord::getWord)
//                .map(string -> '"' + string + '"')
//                .collect(joining(","));
//        query = "[" + query + "]";
//        WordTranslationModel model =
//                translationService
//                        .getTranslation(words.get(0).getLearningLanguage(), words.get(0).getUiLanguage(), query)
//                        .toBlocking().first();
//        return Stream.of(words)
//                .map(word -> getTranslation(word, model))
//                .collect(toList());
//    }

    private VocabularyWord getTranslation(VocabularyWord word, WordTranslationModel model) {
        List<String> strings = model.get(word.getWord());
        if (strings == null) {
            strings = Collections.emptyList();
        }
        return word.withTranslations(strings);
    }

    private class GetCachedDataTask implements Runnable {
        private final Observer<List<VocabularyWord>> observer;

        public GetCachedDataTask(Observer<List<VocabularyWord>> observer) {
            this.observer = observer;
        }

        @Override
        public void run() {
            logger.debug("GetCachedDataTask run() called");
            UserData userData = accountModule.getUserData().get();
            String uiLanguageId = userData.getUiLanguageId();
            String learningLanguageId = userData.getLearningLanguageId();
            List<VocabularyWord> words = provider.getWords(uiLanguageId, learningLanguageId);
            if (!words.isEmpty()) {
                observer.onNext(words);
            }
            logger.debug("GetCachedDataTask run() returned list of size: [{}]", words.size());
        }
    }

    private class LoadWordsTask implements Runnable {
        private final Observer<List<VocabularyWord>> observer;

        public LoadWordsTask(Observer<List<VocabularyWord>> observer) {
            this.observer = observer;
        }

        @Override
        public void run() {
            service.getVocabularyList(System.currentTimeMillis()).enqueue(new SafeCallback<VocabularyResponseModel>() {
                @Override
                public void onResult(VocabularyResponseModel result) {
                    List<VocabularyWord> words = result.toVocabularyData().getWords();
                    observer.onNext(words);
                    executor.execute(() -> provider.putWords(words));
                }

                @Override
                public void onFailure(Call<VocabularyResponseModel> call, Throwable t) {
                    observer.onError(t);
                }
            });
//                    .map(VocabularyResponseModel::toVocabularyData)
//                    .map(VocabularyData::getWords)
//                    .flatMapIterable(list -> list) // split list of item into stream of items
//                    .buffer(10) // group them by 10
//                    .flatMap(list -> Observable.just(list) // for each group translate it in parallel
//                            .subscribeOn(Schedulers.io())
//                            .map(this::translate))
//                    .flatMapIterable(list -> list) // split all groups into one stream
//                    .toList() // group them back to one list
//                    .doOnNext(this::updateCache);
        }
    }

}
