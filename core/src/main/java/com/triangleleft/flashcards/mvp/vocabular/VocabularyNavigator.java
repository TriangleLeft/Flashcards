package com.triangleleft.flashcards.mvp.vocabular;

import android.support.annotation.NonNull;

import com.triangleleft.flashcards.service.vocabular.VocabularyWord;

public interface VocabularyNavigator {
    void onWordSelected(@NonNull VocabularyWord word);
}
