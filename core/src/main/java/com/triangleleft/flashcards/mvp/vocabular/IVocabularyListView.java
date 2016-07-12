package com.triangleleft.flashcards.mvp.vocabular;

import android.support.annotation.NonNull;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;

import java.util.List;

public interface IVocabularyListView extends IView {

    void showWords(@NonNull List<VocabularyWord> words, int selectedPosition);

    void showProgress();

    void showRefreshError();

    void showLoadError();

    void showEmpty();
}
