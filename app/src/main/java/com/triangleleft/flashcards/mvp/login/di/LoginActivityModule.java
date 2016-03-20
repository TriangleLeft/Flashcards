package com.triangleleft.flashcards.mvp.login.di;

import com.triangleleft.flashcards.android.AndroidViewDelegate;
import com.triangleleft.flashcards.dagger.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.common.view.IViewDelegate;
import com.triangleleft.flashcards.mvp.login.presenter.ILoginPresenter;
import com.triangleleft.flashcards.mvp.login.presenter.ILoginPresenterImpl;
import com.triangleleft.flashcards.mvp.login.view.ILoginView;
import com.triangleleft.flashcards.mvp.login.view.ILoginViewDelegate;
import com.triangleleft.flashcards.service.login.ILoginModule;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginActivityModule {

    @ActivityScope
    @Provides
    public ILoginPresenter loginPresenter(@NonNull ILoginModule module,
                                          @NonNull IViewDelegate<ILoginView> viewDelegate) {
        return new ILoginPresenterImpl(module, viewDelegate);
    }

    @ActivityScope
    @Provides
    public IViewDelegate<ILoginView> loginViewDelegate() {
        return new AndroidViewDelegate<>();
    }
}
