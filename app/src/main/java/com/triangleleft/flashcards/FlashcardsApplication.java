package com.triangleleft.flashcards;

import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.dagger.ApplicationComponent;
import com.triangleleft.flashcards.dagger.ApplicationModule;
import com.triangleleft.flashcards.dagger.DaggerApplicationComponent;
import com.triangleleft.flashcards.dagger.NetModule;

import android.app.Application;
import android.support.annotation.NonNull;
import android.widget.Toast;

public class FlashcardsApplication extends Application {

    private ApplicationComponent component;
    private static FlashcardsApplication debugInstace;

    @Override
    public void onCreate() {
        super.onCreate();
        debugInstace = this;
        AssertDialog.init(AssertDialog.AssertMode.DIALOG, getApplicationContext());
        component = buildComponent();
    }

    @NonNull
    protected ApplicationComponent buildComponent() {
        return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this))
                .netModule(new NetModule()).build();
    }

    @NonNull
    public ApplicationComponent getComponent() {
        AssertDialog.assertNotNull(component, "Calling getComponent() before application was created!");
        return component;
    }

    public static void showDebugToast(String text) {
        Toast.makeText(debugInstace, text, Toast.LENGTH_LONG).show();
    }
}
