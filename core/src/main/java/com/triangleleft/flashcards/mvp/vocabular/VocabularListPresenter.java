package com.triangleleft.flashcards.mvp.vocabular;

import com.triangleleft.flashcards.mvp.common.di.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
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
public class VocabularListPresenter extends AbstractPresenter<IVocabularListView> {

    public static final int NO_POSITION = -1;
    private static final Logger logger = LoggerFactory.getLogger(VocabularListPresenter.class);

    private final IVocabularModule vocabularModule;
    private final IVocabularNavigator navigator;
    private final Scheduler mainThreadScheduler;
    private Subscription subscription = Subscriptions.empty();
    private int selectedPosition = NO_POSITION;
    private List<VocabularWord> vocabularList;

    @Inject
    public VocabularListPresenter(IVocabularModule vocabularModule, IVocabularNavigator navigator,
                                  Scheduler mainThreadScheduler) {
        super(IVocabularListView.class);
        this.vocabularModule = vocabularModule;
        this.navigator = navigator;
        this.mainThreadScheduler = mainThreadScheduler;
    }

    @Override
    public void onBind(IVocabularListView view) {
        super.onBind(view);
        if (vocabularList != null) {
            getView().showWords(vocabularList, selectedPosition);
        } else {
            onLoadList();
        }
    }

    public void onWordSelected(int position) {
        selectedPosition = position;
        VocabularWord word = vocabularList.get(position);
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
        subscription = vocabularModule.getVocabularWords(refresh)
                .observeOn(mainThreadScheduler)
                .subscribe(data -> processData(data, refresh), error -> processError(error, refresh));
    }


    private void processData(List<VocabularWord> list, boolean refresh) {
        vocabularList = list;
        // First load
        if (vocabularList.size() > 0 && selectedPosition == NO_POSITION) {
            // Have some words
            // Select first one
            selectedPosition = 0;
        }
        getView().showWords(vocabularList, selectedPosition);
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
