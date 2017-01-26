package com.triangleleft.flashcards.service.cards.rest;

import com.annimon.stream.Stream;
import com.triangleleft.flashcards.Action;
import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardTestResult;
import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.service.cards.FlashcardsModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import static com.annimon.stream.Collectors.toList;

@FunctionsAreNonnullByDefault
public class RestFlashcardsModule implements FlashcardsModule {

    private static final Logger logger = LoggerFactory.getLogger(RestFlashcardsModule.class);

    private final RestService service;
    private final AccountModule accountModule;
    private final VocabularyWordsRepository wordsRepository;
    private final Executor wordsExecutor = Executors.newSingleThreadExecutor();

    @Inject
    public RestFlashcardsModule(RestService service, AccountModule accountModule,
                                VocabularyWordsRepository wordsRepository) {
        this.service = service;
        this.accountModule = accountModule;
        this.wordsRepository = wordsRepository;
    }

    @Override
    public Call<FlashcardTestData> getFlashcards() {
        logger.debug("getFlashcards() called");
        return service.getFlashcardData(accountModule.getWordsAmount(), true, System.currentTimeMillis())
                .map(FlashcardResponseModel::toTestData);
    }

    @Override
    public Call<FlashcardTestData> getLocalFlashcards() {
        // Get from random words from local storage
        return new Call<FlashcardTestData>() {
            @Override
            public void enqueue(Action<FlashcardTestData> onData, Action<Throwable> onError) {
                wordsExecutor.execute(() -> {
                    String uiLangId = accountModule.getUserData().get().getUiLanguageId();
                    String learningLangId = accountModule.getUserData().get().getLearningLanguageId();
                    List<VocabularyWord> words = wordsRepository.getWords(uiLangId, learningLangId);
                    Collections.shuffle(words);
                    List<FlashcardWord> flashcardWords = Stream.of(words)
                            .filterNot(word -> word.getTranslations().isEmpty())
                            .filter(word -> TextUtils.hasText(word.getTranslations().get(0)))
                            .limit(accountModule.getWordsAmount())
                            .map(word -> FlashcardWord
                                    .create(word.getWord(), word.getTranslations().get(0), "fakeId"))
                            .collect(toList());
                    onData.call(FlashcardTestData.create(uiLangId, learningLangId, flashcardWords));
                });
            }

            @Override
            public void cancel() {

            }
        };
    }

    @Override
    public void postResult(FlashcardTestResult result) {
        logger.debug("postResult() called with: result = [{}]", result);
        // NOTE: We don't care about whether we were able to send results
        service.postFlashcardResults(new FlashcardResultsController(result))
                .enqueue(whatever -> {

                }, throwable -> logger.error("Failed to post results", throwable));
    }

}
