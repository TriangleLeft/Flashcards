package com.triangleleft.flashcards.service.vocabular.rest;

import com.annimon.stream.Stream;
import com.triangleleft.flashcards.Action;
import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.Observers;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.TranslationService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Named;

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
    public Call<List<VocabularyWord>> loadVocabularyWords() {
        logger.debug("loadVocabularyWords()");
        return new Call<List<VocabularyWord>>() {
            @Override
            public void enqueue(Action<List<VocabularyWord>> onData, Action<Throwable> onError) {
                mainExecutor.execute(new GetCachedDataTask(Observers.from(onData, onError)));
                mainExecutor.execute(new LoadWordsTask(Observers.from(onData, onError)));
            }

            @Override
            public void cancel() {

            }
        };
    }

    @Override
    public Call<List<VocabularyWord>> refreshVocabularyWords() {
        logger.debug("refreshVocabularyWords()");
        return new Call<List<VocabularyWord>>() {
            @Override
            public void enqueue(Action<List<VocabularyWord>> onData, Action<Throwable> onError) {
                mainExecutor.execute(new LoadWordsTask(Observers.from(onData, onError)));
            }

            @Override
            public void cancel() {

            }
        };
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
            logger.debug("LoadWordsTask run() called");
            service.getVocabularyList(System.currentTimeMillis()).enqueue(model -> {
                        List<VocabularyWord> words = model.toVocabularyData().getWords();
                        mainExecutor.execute(new TranslateWordsTask(words, observer));
                    },
                    observer::onError
            );
        }
    }

    private class TranslateWordsTask implements Runnable {

        private static final int WORDS_PER_GROUP = 10;
        private final List<VocabularyWord> words;
        private final Observer<List<VocabularyWord>> observer;

        public TranslateWordsTask(List<VocabularyWord> words, Observer<List<VocabularyWord>> observer) {
            this.words = words;
            this.observer = observer;
        }

        public void run() {
            // Prepare executor
            ExecutorService executor =
                    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            ExecutorCompletionService<List<VocabularyWord>> completionService =
                    new ExecutorCompletionService<>(executor);
            // Split words into groups of predefined size
            // We do this in order to mimic normal user behavior (and because there is a limit to how long URL can be)
            List<List<VocabularyWord>> groups =
                    Stream.of(words).slidingWindow(WORDS_PER_GROUP, WORDS_PER_GROUP).collect(toList());
            // Submit them to executor
            for (List<VocabularyWord> group : groups) {
                completionService.submit(() -> translate(group));
            }
            List<VocabularyWord> wordsList = new ArrayList<>();
            // Collect results
            for (int i = 0; i < groups.size(); i++) {
                try {
                    wordsList.addAll(completionService.take().get());
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Failed to translated group of words", e);
                }
            }
            // Sort them by normalized words, otherwise all letters with diacritics would be placed at the end
            Collections.sort(wordsList, (o1, o2) -> o1.getNormalizedWord().compareTo(o2.getNormalizedWord()));
            observer.onNext(wordsList);
            provider.putWords(wordsList);
            logger.debug("LoadWordsTask run() returned list of size: [{}]", wordsList.size());
        }
    }

    private List<VocabularyWord> translate(List<VocabularyWord> words) {
        String query = Stream.of(words)
                .map(VocabularyWord::getWord)
                .map(string -> '"' + string + '"')
                .collect(joining(","));
        query = "[" + query + "]";
        CountDownLatch latch = new CountDownLatch(1);
        final List<VocabularyWord> wordsList = new ArrayList<>();
        translationService.getTranslation(words.get(0).getLearningLanguage(), words.get(0).getUiLanguage(), query)
                .enqueue(model -> {
                    for (VocabularyWord word : words) {
                        List<String> strings = model.get(word.getWord());
                        if (strings == null) {
                            strings = Collections.emptyList();
                        }
                        wordsList.add(word.withTranslations(strings));
                    }
                    latch.countDown();
                }, throwable -> {
                });

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return wordsList;
    }
}
