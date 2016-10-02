package com.triangleleft.flashcards.service.cards.stub;

import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardTestResult;
import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.service.cards.FlashcardsModule;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@FunctionsAreNonnullByDefault
public class StubFlashcardsModule implements FlashcardsModule {

    private static final Logger logger = LoggerFactory.getLogger(StubFlashcardsModule.class);

    private final FlashcardTestData testData;

    @Inject
    public StubFlashcardsModule() {
        List<FlashcardWord> words = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            words.add(FlashcardWord.create("word" + i, "translation" + i, "id" + i));
        }
        testData = FlashcardTestData.create("en", "es", words);
    }

    @Override
    public void getFlashcards(Observer<FlashcardTestData> observer) {
        observer.onNext(testData);
    }

    @Override
    public void postResult(FlashcardTestResult results) {
        logger.debug("postResult() called with: results = [{}]", results);
    }
}
