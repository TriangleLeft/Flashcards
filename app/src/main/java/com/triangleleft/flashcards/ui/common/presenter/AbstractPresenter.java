package com.triangleleft.flashcards.ui.common.presenter;

import com.google.common.base.Preconditions;

import com.triangleleft.flashcards.ui.common.view.IView;

import android.support.annotation.NonNull;

public abstract class AbstractPresenter<View extends IView, State extends IPresenterState>
        implements IPresenter<View, State> {

    private View view;

    @Override
    public void onBind(@NonNull View view) {
        Preconditions.checkState(this.view == null, "Presenter is already bound");
        this.view = view;
    }

    @Override
    public void onUnbind(@NonNull View view) {
        Preconditions.checkState(this.view != null, "Presenter is already unbound");
        Preconditions.checkArgument(this.view == view, "Trying to unbind unknown view");
        this.view = null;
    }

    @NonNull
    protected View getView() {
        Preconditions.checkState(view != null, "View is not bound");
        return view;
    }
}
