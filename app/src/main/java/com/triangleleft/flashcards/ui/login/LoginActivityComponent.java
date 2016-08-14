package com.triangleleft.flashcards.ui.login;

import com.triangleleft.flashcards.di.ApplicationComponent;
import com.triangleleft.flashcards.di.IComponent;
import com.triangleleft.flashcards.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface LoginActivityComponent extends IComponent {

    LoginPresenter loginPresenter();

    void inject(LoginActivity loginActivity);
}
