package com.triangleleft.flashcards.mvp.vocabular;

import android.support.annotation.NonNull;

import com.triangleleft.flashcards.service.vocabular.VocabularWord;

public interface IVocabularyNavigator {
    void onWordSelected(@NonNull VocabularWord word);
}
