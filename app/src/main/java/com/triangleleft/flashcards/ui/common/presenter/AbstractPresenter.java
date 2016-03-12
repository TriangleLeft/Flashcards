package com.triangleleft.flashcards.ui.common.presenter;

import com.google.common.base.Preconditions;

import com.triangleleft.flashcards.ui.common.view.IView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

public abstract class AbstractPresenter<View extends IView>
        implements IPresenter<View> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPresenter.class);

    private View view;

    @Override
    public void onBind(@NonNull View view) {
        logger.debug("onBind() called with: view = [{}]", view);
        Preconditions.checkState(this.view == null, "Presenter is already bound");
        this.view = view;
    }

    @Override
    public void onUnbind(@NonNull View view) {
        logger.debug("onUnbind() called with: view = [{}]", view);
        Preconditions.checkState(this.view != null, "Presenter is already unbound");
        Preconditions.checkArgument(this.view == view, "Trying to unbind unknown view");
        this.view = null;
    }

    @NonNull
    protected View getView() {
        logger.debug("getView() called");
        Preconditions.checkState(view != null, "View is not bound");
        return view;
    }
}
