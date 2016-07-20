package com.triangleleft.flashcards.ui.common.view;

import android.support.annotation.NonNull;

public interface IViewAction<View extends IView> {
    void apply(@NonNull View view);
}
