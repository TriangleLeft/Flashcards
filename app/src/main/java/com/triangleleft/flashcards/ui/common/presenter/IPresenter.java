package com.triangleleft.flashcards.ui.common.presenter;

import com.triangleleft.flashcards.ui.common.view.IView;

import android.support.annotation.NonNull;

public interface IPresenter<View extends IView> {
    void onBind(@NonNull View view);

    void onUnbind(@NonNull View view);

    void onCreate();

    void onDestroy();
}
