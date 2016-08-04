package com.triangleleft.flashcards.ui.login;

import com.triangleleft.flashcards.ui.common.di.ApplicationComponent;
import com.triangleleft.flashcards.ui.common.di.component.IComponent;
import com.triangleleft.flashcards.ui.common.di.scope.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface LoginActivityComponent extends IComponent {

    LoginPresenter loginPresenter();

    void inject(LoginActivity loginActivity);
}
