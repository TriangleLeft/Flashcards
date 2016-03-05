package com.triangleleft.flashcards;

import com.triangleleft.flashcards.dagger.ApplicationComponent;
import com.triangleleft.flashcards.dagger.ApplicationModule;
import com.triangleleft.flashcards.dagger.DaggerMockApplicationComponent;
import com.triangleleft.flashcards.dagger.NetModule;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.rest.IDuolingoRest;

import org.mockito.Mockito;

import android.support.annotation.NonNull;

public class MockFlashcardsApplication extends FlashcardsApplication {

    @NonNull
    @Override
    protected ApplicationComponent buildComponent() {
        return DaggerMockApplicationComponent.builder().applicationModule(new ApplicationModule(this) {
            @Override
            public ILoginModule loginModule(IDuolingoRest duolingoRest) {
                return Mockito.mock(ILoginModule.class);
            }
        }).netModule(new NetModule()).build();
    }
}
