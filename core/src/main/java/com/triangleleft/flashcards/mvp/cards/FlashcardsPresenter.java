package com.triangleleft.flashcards.mvp.cards;

import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.service.cards.FlashcardTestResult;
import com.triangleleft.flashcards.service.cards.FlashcardWordResult;
import com.triangleleft.flashcards.service.cards.IFlashcardTestData;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class FlashcardsPresenter extends AbstractPresenter<IFlashcardsView> {

    private static final Logger logger = LoggerFactory.getLogger(FlashcardsPresenter.class);

    private final IFlashcardsModule module;
    private final Scheduler mainThreadScheduler;
    private IFlashcardTestData testData;
    private List<FlashcardWordResult> results = new ArrayList<>();
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
        if (testData == null) {
            onLoadFlashcards();
        } else {
            showFlashcards(testData);
        }
    }

    private void showFlashcards(IFlashcardTestData data) {
        testData = data;
        results.clear();
        if (testData.getWords().size() != 0) {
            getView().showFlashcards(testData.getWords());
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
        results.add(FlashcardWordResult.create(word.getId(), true));
    }

    public void onWordWrong(IFlashcardWord word) {
        logger.debug("onWordWrong() called with: word = [{}]", word);
        results.add(FlashcardWordResult.create(word.getId(), false));
    }

    public void onCardsDepleted() {
        logger.debug("onCardsDepleted() called");
        module.postResult(
                FlashcardTestResult.create(testData.getUiLanguage(), testData.getLearningLanguage(), results));
        getView().showResults();
    }
}
