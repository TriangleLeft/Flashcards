package com.triangleleft.flashcards.ui.cards;

import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.service.cards.FlashcardWordResult;
import com.triangleleft.flashcards.service.cards.FlashcardsModule;
import com.triangleleft.flashcards.ui.ViewEvent;
import com.triangleleft.flashcards.ui.ViewState;
import com.triangleleft.flashcards.ui.common.presenter.AbstractRxPresenter;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

@FunctionsAreNonnullByDefault
@ActivityScope
public class FlashcardsPresenter extends AbstractRxPresenter<FlashcardsView, ViewState, ViewEvent> {

    private static final Logger logger = LoggerFactory.getLogger(FlashcardsPresenter.class);

    private final FlashcardsModule flashcardsModule;
    private final AccountModule accountModule;
    private FlashcardTestData testData;
    private List<FlashcardWordResult> results = new ArrayList<>();
    private Call<FlashcardTestData> call = Call.empty();
    private boolean offlineModeWasProposed = false;
    private boolean offlineMode = false;

    @Inject
    public FlashcardsPresenter(FlashcardsModule flashcardsModule, AccountModule accountModule,
                               @Named(AbstractRxPresenter.UI_SCHEDULER)
                                       Scheduler scheduler) {
        super(scheduler);
        this.flashcardsModule = flashcardsModule;
        this.accountModule = accountModule;
    }

    @Override
    public void onCreate(@Nullable ViewState savedViewState) {
        super.onCreate(savedViewState);
        onLoadFlashcards();
    }


    public void onLoadFlashcards() {
//        applyState(IFlashcardsView::showProgress);
//        results.clear();
//        //call = offlineMode ? flashcardsModule.getLocalFlashcards() : flashcardsModule.getFlashcards();
//        call.enqueue(data -> {
//            if (data.getWords().size() != 0) {
//                testData = data;
//                applyState(view -> view.showWords(data.getWords(), accountModule.getWordsReviewDirection()));
//            } else {
//                // Treat this as error, we expect to always have flashcards
//                processError(new ServerException("Got no flashcards in response"));
//            }
//        }, this::processError);
    }

    private void processError(Throwable throwable) {
//        logger.debug("processError() called with: throwable = [{}]", throwable);
//        // In case we have exception (usually - no internet), ask user whether he wants to start
//        // offline test
//        if (!offlineModeWasProposed) {
//            offlineModeWasProposed = true;
//            applyState(IFlashcardsView::showOfflineModeDialog);
//        } else {
//            // User decided not to, show error as usual
//            applyState(IFlashcardsView::showError);
//        }
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
//        if (!offlineMode) {
//            flashcardsModule.postResult(
//                    FlashcardTestResult.create(testData.getUiLanguage(), testData.getLearningLanguage(), results));
//        }
//        List<FlashcardWord> wrongWords = Stream.of(results)
//                .filterNot(FlashcardWordResult::isCorrect)
//                .map(FlashcardWordResult::getWord)
//                .collect(toList());
//        if (wrongWords.size() == 0) {
//            applyState(IFlashcardsView::showResultsNoErrors);
//        } else {
//            applyState(view -> view.showResultErrors(wrongWords));
//        }
    }

    public void onOfflineModeAccept() {
        // User has accepted to start flashcards in offline mode
        offlineMode = true;
        // Reload flashcards
        onLoadFlashcards();
    }

    public void onOfflineModeDecline() {
        offlineMode = false;
        // Show error state
//        applyState(IFlashcardsView::showError);
    }
}
