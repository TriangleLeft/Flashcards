package com.triangleleft.flashcards.ui.cards;

import com.annimon.stream.Stream;
import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.Subscription;
import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardTestResult;
import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.service.cards.FlashcardWordResult;
import com.triangleleft.flashcards.service.cards.FlashcardsModule;
import com.triangleleft.flashcards.service.common.exception.ServerException;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.annimon.stream.Collectors.toList;

@FunctionsAreNonnullByDefault
@ActivityScope
public class FlashcardsPresenter extends AbstractPresenter<IFlashcardsView> {

    private static final Logger logger = LoggerFactory.getLogger(FlashcardsPresenter.class);

    private final FlashcardsModule module;
    //private final Scheduler mainThreadScheduler;
    private FlashcardTestData testData;
    private List<FlashcardWordResult> results = new ArrayList<>();
    private Subscription subscription = Subscription.EMPTY;

    @Inject
    public FlashcardsPresenter(FlashcardsModule module) {
        super(IFlashcardsView.class);
        this.module = module;
        //this.mainThreadScheduler = mainThreadScheduler;
    }

    @Override
    public void onCreate() {
        onLoadFlashcards();
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
        subscription.unsubscribe();
    }

    public void onLoadFlashcards() {
        applyState(IFlashcardsView::showProgress);
        results.clear();
        subscription.unsubscribe();
        module.getFlashcards(new Observer<FlashcardTestData>() {
            @Override
            public void onError(Throwable e) {
                processError(e);
            }

            @Override
            public void onNext(FlashcardTestData data) {
                if (data.getWords().size() != 0) {
                    testData = data;
                    applyState(view -> view.showWords(data.getWords()));
                } else {
                    // Treat this as error, we expect to always have flashcards
                    processError(new ServerException("Got no flashcards in response"));
                }
            }
        });
    }

    private void processError(Throwable throwable) {
        logger.debug("processError() called with: throwable = [{}]", throwable);
        applyState(IFlashcardsView::showError);
    }

    public void onWordRight(FlashcardWord word) {
        logger.debug("onWordRight() called with: word = [{}]", word);
        results.add(FlashcardWordResult.create(word, true));
    }

    public void onWordWrong(FlashcardWord word) {
        logger.debug("onWordWrong() called with: word = [{}]", word);
        results.add(FlashcardWordResult.create(word, false));
    }

    public void onCardsDepleted() {
        logger.debug("onCardsDepleted() called");
        module.postResult(
                FlashcardTestResult.create(testData.getUiLanguage(), testData.getLearningLanguage(), results));
        List<FlashcardWord> wrongWords = Stream.of(results)
                .filterNot(FlashcardWordResult::isCorrect)
                .map(FlashcardWordResult::getWord)
                .collect(toList());
        if (wrongWords.size() == 0) {
            applyState(IFlashcardsView::showResultsNoErrors);
        } else {
            applyState(view -> view.showResultErrors(wrongWords));
        }
    }

    interface State {
        void apply();
    }
}
