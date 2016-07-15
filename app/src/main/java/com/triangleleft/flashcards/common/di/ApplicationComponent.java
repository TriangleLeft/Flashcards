package com.triangleleft.flashcards.common.di;

import com.triangleleft.flashcards.common.FlagImagesProvider;
import com.triangleleft.flashcards.mvp.FlashcardsNavigator;
import com.triangleleft.flashcards.mvp.common.di.component.IComponent;
import com.triangleleft.flashcards.mvp.common.di.module.NetModule;
import com.triangleleft.flashcards.mvp.common.di.module.RestServiceModule;
import com.triangleleft.flashcards.mvp.common.di.scope.ApplicationScope;
import com.triangleleft.flashcards.mvp.common.presenter.ComponentManager;
import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.util.PersistentStorage;
import dagger.Component;
import rx.Scheduler;

@ApplicationScope
@Component(modules = {ApplicationModule.class, ComponentModule.class, RestServiceModule.class, NetModule.class})
public interface ApplicationComponent extends IComponent {

    LoginModule loginModule();

    VocabularyModule vocabularModule();

    IFlashcardsModule flashcardsModule();

    SettingsModule settingsModule();

    AccountModule accountModule();

    ComponentManager componentManager();

    PersistentStorage persistentStorage();

    IDuolingoRest duolingoRest();

    Scheduler mainThreadScheduler();

    FlagImagesProvider flagImagesProvider();

    FlashcardsNavigator navigator();
}