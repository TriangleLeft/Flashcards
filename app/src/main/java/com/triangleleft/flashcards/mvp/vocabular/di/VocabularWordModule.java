package com.triangleleft.flashcards.mvp.vocabular.di;

import com.triangleleft.flashcards.android.AndroidViewDelegate;
import com.triangleleft.flashcards.dagger.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.common.view.IViewDelegate;
import com.triangleleft.flashcards.mvp.vocabular.IVocabularNavigator;
import com.triangleleft.flashcards.mvp.vocabular.presenter.IVocabularWordPresenter;
import com.triangleleft.flashcards.mvp.vocabular.presenter.IVocabularWordPresenterImpl;
import com.triangleleft.flashcards.mvp.vocabular.view.IVocabularWordView;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public class VocabularWordModule {

    @FragmentScope
    @Provides
    public IVocabularWordPresenter vocabularWordPresenter(@NonNull IVocabularNavigator navigator,
                                                          @NonNull IViewDelegate<IVocabularWordView> delegate) {
        return new IVocabularWordPresenterImpl(delegate, navigator);
    }

    @FragmentScope
    @Provides
    public IViewDelegate<IVocabularWordView> vocabularWordViewDelegate() {
        return new AndroidViewDelegate<>();
    }
}
