package com.triangleleft.flashcards;

import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.dagger.ApplicationComponent;
import com.triangleleft.flashcards.dagger.ApplicationModule;
import com.triangleleft.flashcards.dagger.DaggerApplicationComponent;
import com.triangleleft.flashcards.dagger.DaggerLoginActivityComponent;
import com.triangleleft.flashcards.dagger.LoginActivityComponent;
import com.triangleleft.flashcards.dagger.LoginActivityModule;
import com.triangleleft.flashcards.dagger.NetModule;

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
}
