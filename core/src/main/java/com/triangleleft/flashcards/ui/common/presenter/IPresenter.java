package com.triangleleft.flashcards.ui.common.presenter;

import android.support.annotation.Nullable;

import com.triangleleft.flashcards.ui.common.view.IView;
import com.triangleleft.flashcards.ui.login.ViewState;

public interface IPresenter<View extends IView, VS extends ViewState> {
    @Deprecated
    void onCreate();

    void onCreate(@Nullable VS savedViewState);

    @Deprecated
    void onBind(View view);

    void onRebind(View view);

    void onUnbind();

    void onDestroy();

    View getView();

    VS getViewState();
}
