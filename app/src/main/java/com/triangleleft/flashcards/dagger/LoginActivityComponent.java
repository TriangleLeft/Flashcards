package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.dagger.scope.ActivityScope;
import com.triangleleft.flashcards.android.login.LoginActivity;
import com.triangleleft.flashcards.ui.login.presenter.ILoginPresenter;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = LoginActivityModule.class)
public interface LoginActivityComponent extends IComponent {

    ILoginPresenter loginPresenter();

    void inject(LoginActivity loginActivity);
}
