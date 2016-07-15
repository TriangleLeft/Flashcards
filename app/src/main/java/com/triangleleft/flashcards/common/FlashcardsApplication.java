package com.triangleleft.flashcards.common;

import static com.google.common.base.Preconditions.checkState;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.common.di.ApplicationComponent;
import com.triangleleft.flashcards.common.di.ApplicationModule;
import com.triangleleft.flashcards.common.di.DaggerApplicationComponent;
import com.triangleleft.flashcards.login.LoginActivity;
import com.triangleleft.flashcards.mvp.FlashcardsNavigator;
import com.triangleleft.flashcards.service.common.exception.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;
import timber.log.Timber;

public class FlashcardsApplication extends Application implements FlashcardsNavigator {

    private static final Logger logger = LoggerFactory.getLogger(FlashcardsApplication.class);

    protected static FlashcardsApplication debugInstance;
    private ApplicationComponent component;

    public static void showDebugToast(String text) {
        Toast.makeText(debugInstance, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        debugInstance = this;
        AssertDialog.init(AssertDialog.AssertMode.DIALOG, getApplicationContext());
        component = buildComponent();
        Timber.plant(new Timber.DebugTree());
        Stetho.initializeWithDefaults(this);
        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
            @Override
            public void handleError(Throwable e) {
                logger.error("RxError", e);
                // ConversionException would usually mean that we've got dead cookies
                // (Duolingo would send html page with 200 code
                // So let's try to send user to login
                if (e instanceof ConversionException) {
                    navigateToLogin();
                }
            }
        });
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

    @Override
    public void navigateToLogin() {
        component.accountModule().setUserData(null);
        component.accountModule().setUserId(null);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
