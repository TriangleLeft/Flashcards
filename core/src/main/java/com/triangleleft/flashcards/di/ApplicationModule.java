package com.triangleleft.flashcards.di;

import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.account.SimpleAccountModule;
import com.triangleleft.flashcards.ui.FlashcardsNavigator;
import com.triangleleft.flashcards.ui.common.presenter.ComponentManager;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final FlashcardsNavigator navigator;

    public ApplicationModule(FlashcardsNavigator navigator) {
        this.navigator = navigator;
    }

    @ApplicationScope
    @Provides
    public ComponentManager componentManager() {
        return ComponentManager.buildDefault();
    }

    @ApplicationScope
    @Provides
    public AccountModule accountModule(SimpleAccountModule simpleAccountModule) {
        return simpleAccountModule;
    }

    @ApplicationScope
    @Provides
    public FlashcardsNavigator navigator() {
        return navigator;
    }

}
