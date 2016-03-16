package com.triangleleft.flashcards.mvp.vocabular.presenter;

import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.mvp.main.IMainNavigator;
import com.triangleleft.flashcards.mvp.vocabular.view.IVocabularListView;
import com.triangleleft.flashcards.mvp.vocabular.view.IVocabularListViewDelegate;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.provider.IListener;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.IVocabularRequest;
import com.triangleleft.flashcards.service.vocabular.IVocabularResult;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

public class IVocabularListPresenterImpl extends AbstractPresenter<IVocabularListView>
        implements IVocabularListPresenter {

    private static final Logger logger = LoggerFactory.getLogger(IVocabularListPresenterImpl.class);

    private final IVocabularModule vocabularModule;
    private final IMainNavigator mainNavigator;
    private final IListener<IVocabularResult> vocabularListener = new VocabularListener();
    private IVocabularRequest vocabularRequest;

    public IVocabularListPresenterImpl(@NonNull IVocabularModule vocabularModule,
                                       @NonNull IVocabularListViewDelegate delegate,
                                       @NonNull IMainNavigator mainNavigator) {
        super(delegate, delegate);
        this.vocabularModule = vocabularModule;
        this.mainNavigator = mainNavigator;

        // Start loading
        onRetryClick();
    }

    @Override
    public void onWordSelected(@NonNull IVocabularWord word) {
        logger.debug("onWordSelected() called with: word = [{}]", word);
        mainNavigator.showWord(word);
    }

    @Override
    public void onRetryClick() {
        logger.debug("onRetryClick() called");
        getView().showProgress();
        vocabularRequest = () -> System.currentTimeMillis() + "@" + hashCode();
        vocabularModule.doRequest(vocabularRequest, vocabularListener);
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
        if (vocabularRequest != null) {
            vocabularModule.cancelRequest(vocabularRequest);
            vocabularRequest = null;
        }
    }

    private class VocabularListener implements IListener<IVocabularResult> {
        @Override
        public void onResult(@NonNull IVocabularResult result) {
            logger.debug("onResult() called with: result = [{}]", result);
            vocabularRequest = null;
            getView().showWords(result.getResult());
        }

        @Override
        public void onFailure(@NonNull CommonError error) {
            logger.debug("onFailure() called with: error = [{}]", error);
            vocabularRequest = null;
            getView().showError();
        }
    }
}
