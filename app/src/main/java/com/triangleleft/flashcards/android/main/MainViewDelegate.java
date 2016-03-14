package com.triangleleft.flashcards.android.main;

import com.triangleleft.flashcards.android.AbstractViewDelegate;
import com.triangleleft.flashcards.mvp.main.view.IMainView;
import com.triangleleft.flashcards.mvp.main.view.IMainViewDelegate;

public class MainViewDelegate extends AbstractViewDelegate<IMainView> implements IMainViewDelegate {
    @Override
    public void setTitle(String title) {
        getHandler().post(() -> getView().setTitle(title));
    }
}
