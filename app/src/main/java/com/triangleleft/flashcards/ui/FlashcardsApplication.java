package com.triangleleft.flashcards.ui;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.BuildConfig;
import com.triangleleft.flashcards.di.ApplicationComponent;
import com.triangleleft.flashcards.di.ApplicationModule;
import com.triangleleft.flashcards.di.DaggerApplicationComponent;
import com.triangleleft.flashcards.ui.login.LoginActivity;
import com.triangleleft.flashcards.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class FlashcardsApplication extends Application implements FlashcardsNavigator {

    private static final Logger logger = LoggerFactory.getLogger(FlashcardsApplication.class);

    protected static FlashcardsApplication debugInstance;
    private ApplicationComponent component;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Fabric.with(this, new Crashlytics.Builder().core(core).build());
        refWatcher = LeakCanary.install(this);
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
        Utils.checkState(component != null, "Calling getComponent() before application was created!");
        return component;
    }

    @Override
    public void navigateToLogin() {
        component.accountModule().setUserData(null);
        component.accountModule().setUserId(null);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }
}
