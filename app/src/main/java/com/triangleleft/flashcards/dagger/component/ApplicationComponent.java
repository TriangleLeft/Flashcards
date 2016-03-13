package com.triangleleft.flashcards.dagger.component;

import com.triangleleft.flashcards.android.FlashcardsApplication;
import com.triangleleft.flashcards.dagger.module.ApplicationModule;
import com.triangleleft.flashcards.dagger.module.NetModule;
import com.triangleleft.flashcards.dagger.module.ServiceModule;
import com.triangleleft.flashcards.dagger.scope.ApplicationScope;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.mvp.common.presenter.ComponentManager;
import com.triangleleft.flashcards.util.IPersistentStorage;

import dagger.Component;

@ApplicationScope
@Component(modules = {ApplicationModule.class, ServiceModule.class, NetModule.class})
public interface ApplicationComponent extends IComponent {

    ILoginModule loginModule();

    FlashcardsApplication getApplication();

    ComponentManager componentManager();

    IPersistentStorage persistentStorage();
}