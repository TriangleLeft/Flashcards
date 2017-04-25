package com.triangleleft.flashcards.ui.common.presenter;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.ui.common.view.IView;
import com.triangleleft.flashcards.ui.login.ViewState;

public interface IPresenter<View extends IView, VS extends ViewState> {
    @Deprecated
    void onCreate();

    void onCreate(Optional<VS> savedViewState);

    @Deprecated
    void onBind(View view);

    void onRebind(View view);

    void onUnbind();

    void onDestroy();

    View getView();

    VS getViewState();
}
