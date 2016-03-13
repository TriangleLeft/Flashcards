package com.triangleleft.flashcards.mvp.common.view;

import android.support.annotation.NonNull;

public interface IViewDelegate<View extends IView> {

    void onBind(@NonNull View view);

    void onUnbind();
}
