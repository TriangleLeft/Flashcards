package com.triangleleft.flashcards.dagger.module;

import com.triangleleft.flashcards.android.login.LoginViewDelegate;
import com.triangleleft.flashcards.dagger.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.login.presenter.ILoginPresenter;
import com.triangleleft.flashcards.mvp.login.presenter.ILoginPresenterImpl;
import com.triangleleft.flashcards.mvp.login.view.ILoginViewDelegate;
import com.triangleleft.flashcards.service.login.ILoginModule;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginActivityModule {

    @ActivityScope
    @Provides
    public ILoginPresenter loginPresenter(@NonNull ILoginModule module, @NonNull ILoginViewDelegate viewDelegate) {
        return new ILoginPresenterImpl(module, viewDelegate);
    }

    @ActivityScope
    @Provides
    public ILoginViewDelegate loginViewDelegate() {
        return new LoginViewDelegate();
    }
}
