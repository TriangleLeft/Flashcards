package com.triangleleft.flashcards.mvp.cards;

import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.service.cards.IFlashcardsRequest;
import com.triangleleft.flashcards.service.cards.IFlashcardsResult;
import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.service.common.error.CommonError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

public class FlashcardsPresenter extends AbstractPresenter<IFlashcardsView> {

    private static final Logger logger = LoggerFactory.getLogger(FlashcardsPresenter.class);

    private final IFlashcardsModule module;
    private IFlashcardsRequest request;
    private List<IFlashcardWord> flashcardList;

    @Inject
    public FlashcardsPresenter(IFlashcardsModule module) {
        super(IFlashcardsView.class);
        this.module = module;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        onRestartClick();
    }

    @Override
    public void onBind(IFlashcardsView view) {
        super.onBind(view);
        if (flashcardList == null) {

        } else {
            getView().showFlashcards(flashcardList);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IFlashcardsRequest localRequest = request;
        if (localRequest != null) {
            module.cancelRequest(request);
        }
    }

    public void onRestartClick() {
        getView().showProgress();
        request = () -> String.valueOf(hashCode());
        module.getFlashcards(request, new FlashcardsListener());
    }

    private class FlashcardsListener implements IListener<IFlashcardsResult> {
        @Override
        public void onFailure(CommonError error) {
            logger.debug("onFailure() called with: error = [{}]", error);
            request = null;
        }

        @Override
        public void onResult(IFlashcardsResult result) {
            logger.debug("onResult() called with: result = [{}]", result);
            request = null;
            flashcardList = result.getResult();
            getView().showFlashcards(flashcardList);
        }
    }
}
