package com.triangleleft.flashcards.ui.common.presenter;

import android.support.annotation.Nullable;

import com.triangleleft.flashcards.ui.ViewState;
import com.triangleleft.flashcards.ui.common.view.IView;

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
