package com.triangleleft.flashcards.di.login;

import com.triangleleft.flashcards.di.ApplicationComponent;
import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.ui.login.LoginActivity;
import com.triangleleft.flashcards.ui.login.LoginPresenter;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface LoginActivityComponent extends ApplicationComponent {

    LoginPresenter loginPresenter();

    void inject(LoginActivity loginActivity);
}
