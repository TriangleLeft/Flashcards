package com.triangleleft.flashcards.presenter;

import com.triangleleft.flashcards.view.IView;

import android.os.Bundle;

public interface IPresenter<T extends IView> {
    void onBind(T view);
    void onPause();
    void onResume();

    void onSaveInstanceState(Bundle outState);
}
