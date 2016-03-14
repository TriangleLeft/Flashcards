package com.triangleleft.flashcards.dagger.module;

import com.triangleleft.flashcards.android.vocabular.VocabularViewListDelegate;
import com.triangleleft.flashcards.dagger.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.vocabular.presenter.IVocabularListPresenter;
import com.triangleleft.flashcards.mvp.vocabular.presenter.IVocabularListPresenterImpl;
import com.triangleleft.flashcards.mvp.vocabular.view.IVocabularListViewDelegate;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public class VocabularListModule {

    @FragmentScope
    @Provides
    public IVocabularListPresenter vocabularListPresenter(@NonNull IVocabularListViewDelegate delegate) {
        return new IVocabularListPresenterImpl(delegate);
    }

    @FragmentScope
    @Provides
    public IVocabularListViewDelegate vocabularListViewDelegate() {
        return new VocabularViewListDelegate();
    }
}
