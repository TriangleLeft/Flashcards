package com.triangleleft.flashcards.dagger.module;

import com.triangleleft.flashcards.android.main.MainViewDelegate;
import com.triangleleft.flashcards.dagger.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.main.presenter.IMainPresenter;
import com.triangleleft.flashcards.mvp.main.presenter.IMainPresenterImpl;
import com.triangleleft.flashcards.mvp.main.view.IMainViewDelegate;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    @ActivityScope
    @Provides
    public IMainPresenter mainPresenter(IMainViewDelegate delegate) {
        return new IMainPresenterImpl(delegate);
    }

    @ActivityScope
    @Provides
    public IMainViewDelegate viewDelegate() {
        return new MainViewDelegate();
    }
}
