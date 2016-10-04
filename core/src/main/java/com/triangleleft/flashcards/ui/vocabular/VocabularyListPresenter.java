package com.triangleleft.flashcards.ui.vocabular;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.di.scope.FragmentScope;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;

@FunctionsAreNonnullByDefault
@FragmentScope
public class VocabularyListPresenter extends AbstractPresenter<IVocabularyListView> {

    public static final int NO_POSITION = -1;
    private static final Logger logger = LoggerFactory.getLogger(VocabularyListPresenter.class);

    private final VocabularyModule vocabularyModule;
    private final VocabularyNavigator navigator;
    private int selectedPosition = NO_POSITION;
    private List<VocabularyWord> vocabularyList = Collections.emptyList();
    private boolean hasRefreshError;
    private Call<List<VocabularyWord>> call = Call.empty();

    @Inject
    public VocabularyListPresenter(VocabularyModule vocabularyModule, VocabularyNavigator navigator,
                                   @Named(VIEW_EXECUTOR) Executor executor) {
        super(IVocabularyListView.class, executor);
        this.vocabularyModule = vocabularyModule;
        this.navigator = navigator;
    }

    @Override
    public void onCreate() {
        logger.debug("onCreate() called");
        onLoadList();
    }

    @Override
    public void onBind(IVocabularyListView view) {
        super.onBind(view);
        if (hasRefreshError) {
            getView().showRefreshError();
        }
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
        call.cancel();
    }

    public void onWordSelected(int position) {
        logger.debug("onWordSelected() called with: position = [{}]", position);
        selectedPosition = position;
        VocabularyWord word = vocabularyList.get(position);
        navigator.showWord(Optional.of(word));
    }

    public void onLoadList() {
        logger.debug("onLoadList() called");
        applyState(IVocabularyListView::showProgress);
        // clear list
        vocabularyList.clear();
        // Reset position
        selectedPosition = NO_POSITION;
        call = vocabularyModule.loadVocabularyWords();
        call.enqueue(this::processData, this::processLoadError);
    }

    public void onRefreshList() {
        logger.debug("onRefreshList()");
        // NOTE: we cancel request every time we swipe refresh, not sure it's a good idea
        call.cancel();
        call = vocabularyModule.refreshVocabularyWords();
        call.enqueue(this::processData, this::processRefreshError);
    }

    private void processData(List<VocabularyWord> list) {
        logger.debug("processData() called with list of size = [{}]", list.size());
        vocabularyList = list;
        hasRefreshError = false;
        if (list.isEmpty()) {
            selectedPosition = NO_POSITION;
            applyState(view -> {
                view.showEmpty();
                navigator.showWord(Optional.empty());
            });
        } else {
            if (selectedPosition == NO_POSITION) {
                // Select first one by default
                selectedPosition = 0;
            } else if (selectedPosition > list.size() - 1) {
                // Selected position is outside of list bounds
                selectedPosition = 0;
            }
            applyState(view -> view.showWords(vocabularyList, selectedPosition));
        }
    }

    private void processRefreshError(Throwable error) {
        logger.error("processRefreshError() called with: error = [{}]", error);
        hasRefreshError = true;
        getView().showRefreshError();
    }

    private void processLoadError(Throwable error) {
        logger.error("processLoadError() called with: error = [{}]", error);
        if (vocabularyList.isEmpty()) {
            applyState(IVocabularyListView::showLoadError);
        } else {
            // If we got here that means we were able to show some cached data
            // That's why we don't need to show words list, it should be already shown
            processRefreshError(error);
        }
    }
}
