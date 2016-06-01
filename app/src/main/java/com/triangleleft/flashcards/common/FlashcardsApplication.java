package com.triangleleft.flashcards.common;

import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.AndroidViewDelegate;
import com.triangleleft.flashcards.common.di.ApplicationComponent;
import com.triangleleft.flashcards.common.di.ApplicationModule;
import com.triangleleft.flashcards.common.di.DaggerApplicationComponent;
import com.triangleleft.flashcards.mvp.common.di.module.NetModule;
import com.triangleleft.flashcards.mvp.common.di.module.ServiceModule;
import com.triangleleft.flashcards.mvp.common.view.delegate.ViewDelegateFactory;

import android.app.Application;
import android.support.annotation.NonNull;
import android.widget.Toast;

import timber.log.Timber;

import static com.google.common.base.Preconditions.checkState;

public class FlashcardsApplication extends Application {

    private ApplicationComponent component;
    private static FlashcardsApplication debugInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        debugInstance = this;
        AssertDialog.init(AssertDialog.AssertMode.DIALOG, getApplicationContext());
        ViewDelegateFactory.setDefault(AndroidViewDelegate::new);
        component = buildComponent();
        Timber.plant(new Timber.DebugTree());
    }

    @NonNull
    protected ApplicationComponent buildComponent() {
        return DaggerApplicationComponent.builder()
                .serviceModule(new ServiceModule())
                .applicationModule(new ApplicationModule(this))
                .netModule(new NetModule())
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
