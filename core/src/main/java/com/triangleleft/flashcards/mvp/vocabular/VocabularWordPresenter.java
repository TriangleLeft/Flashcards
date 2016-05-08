package com.triangleleft.flashcards.mvp.vocabular;

import com.triangleleft.flashcards.mvp.common.di.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@FragmentScope
public class VocabularWordPresenter extends AbstractPresenter<IVocabularWordView> {

    private static final Logger logger = LoggerFactory.getLogger(VocabularWordPresenter.class);


    @Inject
    public VocabularWordPresenter() {
        super(IVocabularWordView.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.debug("onCreate() called");
        //  getViewDelegate().post(view -> view.showWord(container.getSelectedWord()));
    }

}
