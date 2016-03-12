package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.FlashcardsApplication;
import com.triangleleft.flashcards.dagger.scope.ApplicationScope;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.ui.common.presenter.ComponentManager;

import dagger.Component;

@ApplicationScope
@Component(modules = {ApplicationModule.class, ServiceModule.class, NetModule.class})
public interface ApplicationComponent extends IComponent {

    ILoginModule loginModule();

    FlashcardsApplication getApplication();

    ComponentManager componentManager();
}