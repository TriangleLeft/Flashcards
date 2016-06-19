package com.triangleleft.flashcards.mvp.vocabular;

import com.triangleleft.flashcards.service.vocabular.VocabularWord;

import android.support.annotation.NonNull;

public interface IVocabularNavigator {
    void onWordSelected(@NonNull VocabularWord word);
}
