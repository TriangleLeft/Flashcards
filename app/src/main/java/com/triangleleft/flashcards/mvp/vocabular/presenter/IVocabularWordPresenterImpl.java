package com.triangleleft.flashcards.mvp.vocabular.presenter;

import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.mvp.common.view.IViewDelegate;
import com.triangleleft.flashcards.mvp.vocabular.view.IVocabularWordView;

import android.support.annotation.NonNull;

public class IVocabularWordPresenterImpl extends AbstractPresenter<IVocabularWordView> {

    public IVocabularWordPresenterImpl(
            @NonNull IViewDelegate<IVocabularWordView> viewDelegate,
            @NonNull IVocabularWordView viewDelegateAsView) {
        super(viewDelegate, viewDelegateAsView);
    }
}
