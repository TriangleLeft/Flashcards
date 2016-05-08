package com.triangleleft.flashcards.mvp.login;

import com.triangleleft.flashcards.android.login.LoginActivity;
import com.triangleleft.flashcards.mvp.common.di.component.ApplicationComponent;
import com.triangleleft.flashcards.mvp.common.di.component.IComponent;
import com.triangleleft.flashcards.mvp.common.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface LoginActivityComponent extends IComponent {

    LoginPresenter loginPresenter();

    void inject(LoginActivity loginActivity);
}
