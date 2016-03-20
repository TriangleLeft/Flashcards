package com.triangleleft.flashcards.mvp.main.navigation;

import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import android.support.annotation.NonNull;

public class ShowWordNavigationRequest implements INavigationRequest {

    private final IVocabularWord word;

    public ShowWordNavigationRequest(@NonNull IVocabularWord word) {
        this.word = word;
    }

    @NonNull
    public IVocabularWord getWord() {
        return word;
    }
}
