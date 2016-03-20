package com.triangleleft.flashcards.mvp.main.di;

import com.triangleleft.flashcards.android.AndroidViewDelegate;
import com.triangleleft.flashcards.dagger.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.common.view.IViewDelegate;
import com.triangleleft.flashcards.mvp.main.presenter.IMainPresenter;
import com.triangleleft.flashcards.mvp.main.presenter.IMainPresenterImpl;
import com.triangleleft.flashcards.mvp.main.view.IMainView;
import com.triangleleft.flashcards.mvp.vocabular.IVocabularNavigator;

import dagger.Module;
import dagger.Provides;

@Module
public class MainPageModule {

    @ActivityScope
    @Provides
    public IMainPresenter mainPresenter(IViewDelegate<IMainView> delegate) {
        return new IMainPresenterImpl(delegate);
    }

    @ActivityScope
    @Provides
    public IVocabularNavigator vocabularNavigator(IMainPresenter presenter) {
        return presenter;
    }

    @ActivityScope
    @Provides
    public IViewDelegate<IMainView> viewDelegate() {
        return new AndroidViewDelegate<>();
    }
}
