package com.triangleleft.flashcards.mvp.main.view;

import com.triangleleft.flashcards.mvp.common.view.IView;

import android.support.annotation.NonNull;

public interface IMainView extends IView {

    void setTitle(String title);

    void setPage(@NonNull MainViewPage state);
}
