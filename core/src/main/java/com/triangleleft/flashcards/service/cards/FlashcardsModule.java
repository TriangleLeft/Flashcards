package com.triangleleft.flashcards.service.cards;

import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;


@FunctionsAreNonnullByDefault
public interface FlashcardsModule {

    Call<FlashcardTestData> getFlashcards();

    Call<FlashcardTestData> getLocalFlashcards();

    void postResult(FlashcardTestResult results);
}
