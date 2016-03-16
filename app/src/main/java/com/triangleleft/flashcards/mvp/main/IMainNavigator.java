package com.triangleleft.flashcards.mvp.main;

import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import android.support.annotation.NonNull;

public interface IMainNavigator {

    void showWord(@NonNull IVocabularWord word);
}
