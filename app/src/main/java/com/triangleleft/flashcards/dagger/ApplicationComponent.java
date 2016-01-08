package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.FlashcardsApplication;
import com.triangleleft.flashcards.dagger.scope.ApplcationScope;
import com.triangleleft.flashcards.service.ILoginModule;

import android.content.SharedPreferences;

import dagger.Component;

@ApplcationScope
@Component(modules = {ApplicationModule.class, NetModule.class})
public interface ApplicationComponent {

    ILoginModule loginModule();

    FlashcardsApplication getApplication();

    SharedPreferences getPreferences();

    NetComponent netComponent(NetModule netModule);

}