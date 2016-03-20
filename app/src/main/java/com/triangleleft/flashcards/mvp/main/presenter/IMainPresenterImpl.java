package com.triangleleft.flashcards.mvp.main.presenter;

import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.mvp.common.view.IViewDelegate;
import com.triangleleft.flashcards.mvp.main.view.IMainView;
import com.triangleleft.flashcards.mvp.main.view.MainViewPage;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

public class IMainPresenterImpl extends AbstractPresenter<IMainView> implements IMainPresenter {

    private static final Logger logger = LoggerFactory.getLogger(IMainPresenterImpl.class);

    private IVocabularWord selectedWord;

    public IMainPresenterImpl(@NonNull IViewDelegate<IMainView> viewDelegate) {
        super(viewDelegate);
        logger.debug("IMainPresenterImpl() called with: viewDelegate = [{}]", viewDelegate);
    }

    @Override
    public void onWordSelected(@NonNull IVocabularWord word) {
        logger.debug("onWordSelected() called with: word = [{}]", word);
        selectedWord = word;
        getViewDelegate().post(view -> view.setPage(MainViewPage.WORD));
    }

    @Override
    @NonNull
    public IVocabularWord getSelectedWord() {
        logger.debug("getSelectedWord() called");
        return selectedWord;
    }
}
