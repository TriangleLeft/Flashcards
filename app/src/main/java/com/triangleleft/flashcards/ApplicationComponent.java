package com.triangleleft.flashcards;

import com.triangleleft.flashcards.service.IFlashcardsService;
import com.triangleleft.flashcards.service.stub.StubLoginModule;
import com.triangleleft.flashcards.vocab.VocabularListFragment;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);
    void inject(LoginActivity activity);

    IFlashcardsService getService();
    FlashcardsApplication getApplication();
    SharedPreferences getPreferences();

    void inject(StubLoginModule stubLoginModule);

    void inject(MainActivity mainActivity);

    void inject(VocabularListFragment vocabularListFragment);
}