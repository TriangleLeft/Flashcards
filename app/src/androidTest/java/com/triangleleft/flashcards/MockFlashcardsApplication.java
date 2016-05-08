package com.triangleleft.flashcards;


import com.triangleleft.flashcards.android.FlashcardsApplication;
import com.triangleleft.flashcards.mvp.common.di.component.ApplicationComponent;
import com.triangleleft.flashcards.mvp.common.di.component.DaggerApplicationComponent;
import com.triangleleft.flashcards.mvp.common.di.module.ApplicationModule;
import com.triangleleft.flashcards.mvp.common.di.module.NetModule;
import com.triangleleft.flashcards.mvp.common.di.module.ServiceModule;
import com.triangleleft.flashcards.service.IDuolingoRest;

import org.mockito.Mockito;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;

public class MockFlashcardsApplication extends FlashcardsApplication {

    @NonNull
    @Override
    protected ApplicationComponent buildComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .serviceModule(new ServiceModule())
                .netModule(new NetModule() {
                    @Override
                    public IDuolingoRest duolingoRest(Retrofit retrofit) {
                        return Mockito.mock(IDuolingoRest.class);
                    }
                })
                .build();
    }

}
