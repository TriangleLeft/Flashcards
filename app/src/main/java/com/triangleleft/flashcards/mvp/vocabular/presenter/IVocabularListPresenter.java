package com.triangleleft.flashcards.mvp.vocabular.presenter;

import com.triangleleft.flashcards.mvp.common.presenter.IPresenter;
import com.triangleleft.flashcards.mvp.vocabular.view.IVocabularListView;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import android.support.annotation.NonNull;

public interface IVocabularListPresenter extends IPresenter<IVocabularListView> {
    void onWordSelected(@NonNull IVocabularWord word);

    void onRetryClick();
}
