package com.triangleleft.flashcards.android;

import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.dagger.component.ApplicationComponent;
import com.triangleleft.flashcards.dagger.component.DaggerApplicationComponent;
import com.triangleleft.flashcards.dagger.module.ApplicationModule;
import com.triangleleft.flashcards.dagger.module.NetModule;
import com.triangleleft.flashcards.mvp.login.di.DaggerLoginActivityComponent;
import com.triangleleft.flashcards.mvp.login.di.LoginActivityComponent;
import com.triangleleft.flashcards.mvp.login.di.LoginActivityModule;
import com.triangleleft.flashcards.mvp.main.di.DaggerMainPageComponent;
import com.triangleleft.flashcards.mvp.main.di.MainPageComponent;
import com.triangleleft.flashcards.mvp.main.di.MainPageModule;
import com.triangleleft.flashcards.mvp.vocabular.di.DaggerVocabularListComponent;
import com.triangleleft.flashcards.mvp.vocabular.di.DaggerVocabularWordComponent;
import com.triangleleft.flashcards.mvp.vocabular.di.VocabularListComponent;
import com.triangleleft.flashcards.mvp.vocabular.di.VocabularListModule;
import com.triangleleft.flashcards.mvp.vocabular.di.VocabularWordComponent;
import com.triangleleft.flashcards.mvp.vocabular.di.VocabularWordModule;

import android.app.Application;
import android.support.annotation.NonNull;
import android.widget.Toast;

import timber.log.Timber;

import static com.google.common.base.Preconditions.checkState;

public class FlashcardsApplication extends Application {

    private ApplicationComponent component;
    private static FlashcardsApplication debugInstace;

    @Override
    public void onCreate() {
        super.onCreate();
        debugInstace = this;
        AssertDialog.init(AssertDialog.AssertMode.DIALOG, getApplicationContext());
        component = buildComponent();
        Timber.plant(new Timber.DebugTree());
    }

    @NonNull
    protected ApplicationComponent buildComponent() {
        return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this))
                .netModule(new NetModule()).build();
    }

    @NonNull
    public ApplicationComponent getComponent() {
        checkState(component != null, "Calling getComponent() before application was created!");
        return component;
    }

    public static void showDebugToast(String text) {
        Toast.makeText(debugInstace, text, Toast.LENGTH_LONG).show();
    }

    public LoginActivityComponent buildLoginActivityComponent() {
        return DaggerLoginActivityComponent.builder().applicationComponent(getComponent())
                .loginActivityModule(new LoginActivityModule()).build();
    }

    public MainPageComponent buildMainActivityComponent() {
        return DaggerMainPageComponent.builder().applicationComponent(getComponent())
                .mainPageModule(new MainPageModule()).build();
    }

    public VocabularListComponent buildVocabularListComponent(MainPageComponent component) {
        return DaggerVocabularListComponent.builder().mainPageComponent(component)
                .vocabularListModule(new VocabularListModule()).build();
    }

    public VocabularWordComponent buildVocabularWordComponent(MainPageComponent component) {
        return DaggerVocabularWordComponent.builder().mainPageComponent(component)
                .vocabularWordModule(new VocabularWordModule()).build();
    }
}
