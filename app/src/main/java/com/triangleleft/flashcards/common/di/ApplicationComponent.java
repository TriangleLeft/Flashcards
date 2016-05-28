package com.triangleleft.flashcards.common.di;

import com.triangleleft.flashcards.common.FlashcardsApplication;
import com.triangleleft.flashcards.mvp.common.di.component.IComponent;
import com.triangleleft.flashcards.mvp.common.di.module.NetModule;
import com.triangleleft.flashcards.mvp.common.di.module.StubServiceModule;
import com.triangleleft.flashcards.mvp.common.di.scope.ApplicationScope;
import com.triangleleft.flashcards.mvp.common.presenter.ComponentManager;
import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.util.IPersistentStorage;

import dagger.Component;
import rx.Scheduler;

@ApplicationScope
@Component(modules = {ApplicationModule.class, StubServiceModule.class, NetModule.class})
public interface ApplicationComponent extends IComponent {

    ILoginModule loginModule();

    IVocabularModule vocabularModule();

    IFlashcardsModule flashcardsModule();

    FlashcardsApplication getApplication();

    ComponentManager componentManager();

    IPersistentStorage persistentStorage();

    IDuolingoRest duolingoRest();

    Scheduler mainThreadScheduler();
}