package com.triangleleft.flashcards.mvp.common.presenter;

import com.triangleleft.flashcards.mvp.common.view.IView;

import android.support.annotation.NonNull;

public interface IPresenter<View extends IView> {
    void onBind(@NonNull View view);

    void onUnbind();

    void onDestroy();
}
