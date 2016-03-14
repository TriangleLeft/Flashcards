package com.triangleleft.flashcards.mvp.main.presenter;

import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.mvp.main.view.IMainView;
import com.triangleleft.flashcards.mvp.main.view.IMainViewDelegate;

import android.support.annotation.NonNull;

public class IMainPresenterImpl extends AbstractPresenter<IMainView> implements IMainPresenter {

    public IMainPresenterImpl(@NonNull IMainViewDelegate viewDelegate) {
        super(viewDelegate, viewDelegate);
    }
}
