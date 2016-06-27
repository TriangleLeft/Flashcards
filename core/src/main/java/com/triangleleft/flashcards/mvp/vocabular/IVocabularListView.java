package com.triangleleft.flashcards.mvp.vocabular;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;

import android.support.annotation.NonNull;

import java.util.List;

public interface IVocabularListView extends IView {

    void showWords(@NonNull List<VocabularWord> words, int selectedPosition);

    void showProgress();

    void showError();

    void showErrorNoContent();
}
