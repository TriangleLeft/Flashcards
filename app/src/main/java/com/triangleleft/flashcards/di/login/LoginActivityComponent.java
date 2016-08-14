package com.triangleleft.flashcards.di.login;

import com.triangleleft.flashcards.di.AndroidApplicationComponent;
import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.ui.login.LoginActivity;
import com.triangleleft.flashcards.ui.login.LoginPresenter;

import dagger.Component;

@ActivityScope
@Component(dependencies = AndroidApplicationComponent.class)
public interface LoginActivityComponent extends AndroidApplicationComponent {

    LoginPresenter loginPresenter();

    void inject(LoginActivity loginActivity);
}
