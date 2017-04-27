package com.triangleleft.flashcards.di;

import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.TranslationService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.cards.FlashcardsModule;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.ui.FlashcardsNavigator;
import com.triangleleft.flashcards.ui.common.ComponentManager;
import com.triangleleft.flashcards.ui.common.FlagImagesProvider;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.util.PersistentStorage;

import java.util.concurrent.Executor;

import javax.inject.Named;

import dagger.Component;
import io.reactivex.Scheduler;

@ApplicationScope
@Component(modules = {ApplicationModule.class, NetworkModule.class, StubServiceModule.class})
public interface ApplicationComponent extends IComponent {

    LoginModule loginModule();

    VocabularyModule vocabularModule();

    FlashcardsModule flashcardsModule();

    SettingsModule settingsModule();

    AccountModule accountModule();

    ComponentManager componentManager();

    PersistentStorage persistentStorage();

    RestService restService();

    TranslationService translationService();

    FlashcardsNavigator navigator();

    FlagImagesProvider flagImagesProvider();

    @Named(AbstractPresenter.UI_SCHEDULER)
    Scheduler uiScheduler();

    @Named(AbstractPresenter.VIEW_EXECUTOR)
    Executor uiThreadExecutor();
}