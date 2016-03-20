package com.triangleleft.flashcards.mvp.vocabular.di;

import com.triangleleft.flashcards.android.AndroidViewDelegate;
import com.triangleleft.flashcards.dagger.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.common.view.IViewDelegate;
import com.triangleleft.flashcards.mvp.vocabular.IVocabularNavigator;
import com.triangleleft.flashcards.mvp.vocabular.presenter.IVocabularListPresenter;
import com.triangleleft.flashcards.mvp.vocabular.presenter.IVocabularListPresenterImpl;
import com.triangleleft.flashcards.mvp.vocabular.view.IVocabularListView;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public class VocabularListModule {

    @FragmentScope
    @Provides
    public IVocabularListPresenter vocabularListPresenter(@NonNull IVocabularModule module,
                                                          @NonNull IVocabularNavigator navigator,
                                                          @NonNull IViewDelegate<IVocabularListView> delegate) {
        return new IVocabularListPresenterImpl(module, delegate, navigator);
    }

    @FragmentScope
    @Provides
    public IViewDelegate<IVocabularListView> vocabularListViewDelegate() {
        return new AndroidViewDelegate<>();
    }
}
