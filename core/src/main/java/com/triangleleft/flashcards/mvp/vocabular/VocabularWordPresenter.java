package com.triangleleft.flashcards.mvp.vocabular;

import com.triangleleft.flashcards.mvp.common.di.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;

import javax.inject.Inject;

@FragmentScope
public class VocabularWordPresenter extends AbstractPresenter<IVocabularWordView> {

    private VocabularWord word;

    @Inject
    public VocabularWordPresenter() {
        super(IVocabularWordView.class);
    }

    @Override
    public void onBind(IVocabularWordView view) {
        super.onBind(view);
        if (word != null) {
            getView().showWord(word);
        }
    }

    public void setWord(VocabularWord word) {
        this.word = word;
        getView().showWord(word);
    }
}
