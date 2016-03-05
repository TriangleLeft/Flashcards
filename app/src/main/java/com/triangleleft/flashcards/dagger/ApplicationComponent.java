package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.FlashcardsApplication;
import com.triangleleft.flashcards.dagger.scope.ApplicationScope;
import com.triangleleft.flashcards.service.login.ILoginModule;

import dagger.Component;

@ApplicationScope
@Component(modules = {ApplicationModule.class, ServiceModule.class, NetModule.class})
public interface ApplicationComponent {

    ILoginModule loginModule();

    FlashcardsApplication getApplication();

}