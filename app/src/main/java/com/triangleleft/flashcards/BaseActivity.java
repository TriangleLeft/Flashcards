package com.triangleleft.flashcards;

import com.triangleleft.flashcards.dagger.ApplicationComponent;

import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected ApplicationComponent getApplicationComponent() {
        return ((FlashcardsApplication)getApplication()).getComponent();
    }
    
}
