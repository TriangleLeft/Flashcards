package com.triangleleft.flashcards.ui.vocabular;

import android.support.annotation.NonNull;

import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.common.view.IView;

import java.util.List;

public interface IVocabularyListView extends IView {

    void showWords(@NonNull List<VocabularyWord> words, int selectedPosition);

    void showProgress();

    void showRefreshError();

    void showLoadError();

    void showEmpty();
}
