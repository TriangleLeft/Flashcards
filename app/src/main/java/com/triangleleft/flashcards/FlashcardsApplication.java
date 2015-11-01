package com.triangleleft.flashcards;

import com.triangleleft.assertdialog.AssertDialog;

import android.app.Application;

public class FlashcardsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.INSTANCE.setup(this);
        AssertDialog.init(AssertDialog.AssertMode.DIALOG, getApplicationContext());

    }
}
