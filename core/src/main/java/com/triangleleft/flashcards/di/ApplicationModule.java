package com.triangleleft.flashcards.di;

import com.google.gson.Gson;

import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.account.SimpleAccountModule;
import com.triangleleft.flashcards.ui.common.presenter.ComponentManager;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

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

    @Provides
    public Gson gson() {
        return new Gson();
    }


}
