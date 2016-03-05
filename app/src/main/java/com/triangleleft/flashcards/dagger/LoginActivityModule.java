package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.dagger.scope.ActivityScope;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.ui.login.presenter.ILoginPresenterImpl;
import com.triangleleft.flashcards.ui.login.presenter.ILoginPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginActivityModule {

    @ActivityScope
    @Provides
    public ILoginPresenter loginPresenter(ILoginModule module) {
        return new ILoginPresenterImpl(module);
    }
}
