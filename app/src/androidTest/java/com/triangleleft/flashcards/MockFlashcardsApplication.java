package com.triangleleft.flashcards;

import com.triangleleft.flashcards.dagger.ApplicationComponent;
import com.triangleleft.flashcards.dagger.ApplicationModule;
import com.triangleleft.flashcards.dagger.DaggerApplicationComponent;
import com.triangleleft.flashcards.dagger.NetModule;
import com.triangleleft.flashcards.dagger.ServiceModule;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.rest.IDuolingoRest;

import org.mockito.Mockito;

import android.support.annotation.NonNull;

public class MockFlashcardsApplication extends FlashcardsApplication {

    @NonNull
    @Override
    protected ApplicationComponent buildComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .serviceModule(new ServiceModule() {
                    @Override
                    public ILoginModule loginModule(IDuolingoRest service) {
                        return Mockito.mock(ILoginModule.class);
                    }
                })
                .netModule(new NetModule())
                .build();
    }
}
