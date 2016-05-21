package com.triangleleft.flashcards.mvp.cards;

import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class FlashcardsPresenter extends AbstractPresenter<IFlashcardsView> {

    private static final Logger logger = LoggerFactory.getLogger(FlashcardsPresenter.class);

    private final IFlashcardsModule module;
    private final Scheduler mainThreadScheduler;
    private List<IFlashcardWord> flashcardList;
    private Subscription subscription = Subscriptions.empty();

    @Inject
    public FlashcardsPresenter(IFlashcardsModule module, Scheduler mainThreadScheduler) {
        super(IFlashcardsView.class);
        this.module = module;
        this.mainThreadScheduler = mainThreadScheduler;
    }

    @Override
    public void onBind(IFlashcardsView view) {
        super.onBind(view);
        if (flashcardList == null) {
            onLoadFlashcards();
        } else {
            showFlashcards(flashcardList);
        }
    }

    private void showFlashcards(List<IFlashcardWord> list) {
        flashcardList = list;
        if (flashcardList.size() != 0) {
            getView().showFlashcards(flashcardList);
        } else {
            // We expect to always have flashcards
            getView().showErrorNoContent();
        }
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
        subscription.unsubscribe();
    }

    public void onLoadFlashcards() {
        getView().showProgress();
        subscription.unsubscribe();
        subscription = module.getFlashcards()
                .observeOn(mainThreadScheduler)
                .subscribe(
                        this::showFlashcards,
                        error -> {
                            error.printStackTrace();
                            getView().showErrorNoContent();
                        }
                );
    }

    public void onWordRight(IFlashcardWord word) {
        logger.debug("onWordRight() called with: word = [{}]", word);
    }

    public void onWordWrong(IFlashcardWord word) {
        logger.debug("onWordWrong() called with: word = [{}]", word);

    }

    public void onCardsDepleted() {
        logger.debug("onCardsDepleted() called");
        // TODO: send results
        getView().showResults();
    }
}
