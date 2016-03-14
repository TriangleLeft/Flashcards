package com.triangleleft.flashcards.mvp.vocabular.presenter;

import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.mvp.vocabular.view.IVocabularListView;
import com.triangleleft.flashcards.mvp.vocabular.view.IVocabularListViewDelegate;

import android.support.annotation.NonNull;

public class IVocabularListPresenterImpl extends AbstractPresenter<IVocabularListView>
        implements IVocabularListPresenter {
    public IVocabularListPresenterImpl(
            @NonNull IVocabularListViewDelegate delegate) {
        super(delegate, delegate);
    }
}
