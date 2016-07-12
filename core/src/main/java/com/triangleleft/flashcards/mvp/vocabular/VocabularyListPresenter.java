package com.triangleleft.flashcards.mvp.vocabular;

import com.triangleleft.flashcards.mvp.common.di.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.service.vocabular.IVocabularyModule;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

@FunctionsAreNonnullByDefault
@FragmentScope
public class VocabularyListPresenter extends AbstractPresenter<IVocabularyListView> {

    public static final int NO_POSITION = -1;
    private static final Logger logger = LoggerFactory.getLogger(VocabularyListPresenter.class);

    private final IVocabularyModule vocabularyModule;
    private final IVocabularyNavigator navigator;
    private final Scheduler mainThreadScheduler;
    private Subscription subscription = Subscriptions.empty();
    private int selectedPosition = NO_POSITION;
    private List<VocabularWord> vocabularyList;

    @Inject
    public VocabularyListPresenter(IVocabularyModule vocabularyModule, IVocabularyNavigator navigator,
                                   Scheduler mainThreadScheduler) {
        super(IVocabularyListView.class);
        this.vocabularyModule = vocabularyModule;
        this.navigator = navigator;
        this.mainThreadScheduler = mainThreadScheduler;
    }

    @Override
    public void onBind(IVocabularyListView view) {
        super.onBind(view);
        if (vocabularyList != null) {
            getView().showWords(vocabularyList, selectedPosition);
        } else {
            onLoadList();
        }
    }

    public void onWordSelected(int position) {
        selectedPosition = position;
        VocabularWord word = vocabularyList.get(position);
        navigator.onWordSelected(word);
    }

    public void onLoadList() {
        logger.debug("onLoadList() called");
        getView().showProgress();
        loadList(false);
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
        subscription.unsubscribe();
    }

    public void onRefreshList() {
        loadList(true);
    }

    private void loadList(boolean refresh) {
        subscription.unsubscribe();
        subscription = vocabularyModule.getVocabularWords(refresh)
                .observeOn(mainThreadScheduler)
                .subscribe(data -> processData(data, refresh), error -> processError(error, refresh));
    }


    private void processData(List<VocabularWord> list, boolean refresh) {
        vocabularyList = list;
        // First load
        if (vocabularyList.size() > 0 && selectedPosition == NO_POSITION) {
            // Have some words
            // Select first one
            selectedPosition = 0;
        }
        getView().showWords(vocabularyList, selectedPosition);
    }

    private void processError(Throwable error, boolean refresh) {
        logger.error("processError() ", error);
        logger.error("processError() called with: error = [{}], refresh = [{}]", error, refresh);

        if (refresh) {
            getView().showError();
        } else {
            getView().showErrorNoContent();
        }
    }
}
