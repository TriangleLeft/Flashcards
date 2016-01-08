package com.triangleleft.flashcards.presenter;

import com.triangleleft.flashcards.view.IView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface IPresenter<T extends IView> {
    void onBind(@NonNull T view);
    void onPause();
    void onResume();

    void onRestoreInstanceState(@Nullable Bundle inState);
    void onSaveInstanceState(@NonNull Bundle outState);
}
