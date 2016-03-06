package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.dagger.scope.ApplicationScope;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.rest.IDuolingoRest;
import com.triangleleft.flashcards.service.rest.RestLoginModule;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @ApplicationScope
    @Provides
    public ILoginModule loginModule(IDuolingoRest service) {
        return new RestLoginModule(service);
    }
}
