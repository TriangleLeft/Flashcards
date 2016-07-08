package com.triangleleft.flashcards.common;

import android.app.Application;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.common.di.ApplicationComponent;
import com.triangleleft.flashcards.common.di.ApplicationModule;
import com.triangleleft.flashcards.common.di.DaggerApplicationComponent;

import timber.log.Timber;

import static com.google.common.base.Preconditions.checkState;

public class FlashcardsApplication extends Application {

    private ApplicationComponent component;
    protected static FlashcardsApplication debugInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        debugInstance = this;
        AssertDialog.init(AssertDialog.AssertMode.DIALOG, getApplicationContext());
        component = buildComponent();
        Timber.plant(new Timber.DebugTree());
        Stetho.initializeWithDefaults(this);
    }

    @NonNull
    protected ApplicationComponent buildComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    @NonNull
    public ApplicationComponent getComponent() {
        checkState(component != null, "Calling getComponent() before application was created!");
        return component;
    }

    public static void showDebugToast(String text) {
        Toast.makeText(debugInstance, text, Toast.LENGTH_LONG).show();
    }

}
