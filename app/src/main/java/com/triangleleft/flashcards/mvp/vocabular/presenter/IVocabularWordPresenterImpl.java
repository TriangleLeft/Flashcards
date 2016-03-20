package com.triangleleft.flashcards.mvp.vocabular.presenter;

import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.mvp.common.view.IViewDelegate;
import com.triangleleft.flashcards.mvp.vocabular.IVocabularNavigator;
import com.triangleleft.flashcards.mvp.vocabular.view.IVocabularWordView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

public class IVocabularWordPresenterImpl extends AbstractPresenter<IVocabularWordView>
        implements IVocabularWordPresenter {

    private static final Logger logger = LoggerFactory.getLogger(IVocabularWordPresenterImpl.class);

    private final IVocabularNavigator navigator;

    public IVocabularWordPresenterImpl(
            @NonNull IViewDelegate<IVocabularWordView> viewDelegate,
            @NonNull IVocabularNavigator navigator) {
        super(viewDelegate);
        this.navigator = navigator;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.debug("onCreate() called");
        getViewDelegate().post(view -> view.showWord(navigator.getSelectedWord()));
    }
}
