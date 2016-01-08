package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.FlashcardsApplication;
import com.triangleleft.flashcards.dagger.scope.ApplcationScope;
import com.triangleleft.flashcards.service.IFlashcardsService;
import com.triangleleft.flashcards.service.ILoginModule;
import com.triangleleft.flashcards.service.IVocabularModule;
import com.triangleleft.flashcards.service.rest.IDuolingoRest;
import com.triangleleft.flashcards.service.rest.RestLoginModule;
import com.triangleleft.flashcards.service.stub.StubFlashcardsService;
import com.triangleleft.flashcards.service.stub.StubVocabularModule;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final FlashcardsApplication application;

    public ApplicationModule(FlashcardsApplication application) {
        this.application = application;
    }

    @ApplcationScope
    @Provides
    public FlashcardsApplication application() {
        return application;
    }

    @ApplcationScope
    @Provides
    public IFlashcardsService service() {
        return new StubFlashcardsService();
    }

    @ApplcationScope
    @Provides
    public IVocabularModule vocabularModule() {
        return new StubVocabularModule();
    }

    @ApplcationScope
    @Provides
    public ILoginModule loginModule(IDuolingoRest duolingoRest) {
        return new RestLoginModule(duolingoRest);
    }

    @ApplcationScope
    @Provides
    public SharedPreferences preferences() {
        return application.getSharedPreferences(ApplicationModule.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
}
