package com.triangleleft.flashcards.mvp.common.view.delegate;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.mvp.common.view.IViewAction;

import android.support.annotation.NonNull;

public interface IViewDelegate<View extends IView> {

    void onBind(@NonNull View view);

    void onUnbind();

    void post(@NonNull IViewAction<View> action);

}
