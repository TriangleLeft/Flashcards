package com.triangleleft.flashcards.ui.common.presenter;

import com.triangleleft.flashcards.ui.common.view.IView;

public interface IPresenter<View extends IView> {
    void onCreate();

    void onBind(View view);

    void onRebind(View view);

    void onUnbind();

    void onDestroy();

    View getView();
}
