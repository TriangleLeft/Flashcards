package com.triangleleft.flashcards.ui.common.presenter;

import com.triangleleft.flashcards.ui.common.view.IView;

import android.support.annotation.NonNull;

public interface IPresenter<View extends IView, State extends IPresenterState> {
    void onBind(@NonNull View view);

    void onUnbind(@NonNull View view);
    void onPause();
    void onResume();

    void onRestoreInstanceState(@NonNull State inState);

    void onSaveInstanceState(@NonNull State outState);

    void onCreateInstanceState();
}
