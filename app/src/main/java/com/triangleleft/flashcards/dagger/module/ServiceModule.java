package com.triangleleft.flashcards.dagger.module;

import com.triangleleft.flashcards.dagger.scope.ApplicationScope;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.rest.IDuolingoRest;
import com.triangleleft.flashcards.service.rest.RestLoginModule;
import com.triangleleft.flashcards.service.rest.RestVocabularModule;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.util.IPersistentStorage;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @ApplicationScope
    @Provides
    public ILoginModule loginModule(@NonNull IDuolingoRest service, @NonNull IPersistentStorage storage) {
        return new RestLoginModule(service, storage);
    }

    @ApplicationScope
    @Provides
    public IVocabularModule vocabularModule(@NonNull IDuolingoRest service) {
        return new RestVocabularModule(service);
    }
}
