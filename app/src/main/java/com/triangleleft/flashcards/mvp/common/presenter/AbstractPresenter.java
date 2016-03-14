package com.triangleleft.flashcards.mvp.common.presenter;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.mvp.common.view.IViewDelegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

public abstract class AbstractPresenter<View extends IView>
        implements IPresenter<View> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPresenter.class);
    private final IViewDelegate<View> viewDelegate;
    private final View viewDelegateAsView;

    public AbstractPresenter(@NonNull IViewDelegate<View> viewDelegate, @NonNull View viewDelegateAsView) {
        this.viewDelegate = viewDelegate;
        this.viewDelegateAsView = viewDelegateAsView;
    }

    @Override
    public void onBind(@NonNull View view) {
        logger.debug("onBind() called with: view = [{}]", view);
        viewDelegate.onBind(view);
        //     Preconditions.checkState(this.view == null, "Presenter is already bound");
    }

    @Override
    public void onUnbind() {
        logger.debug("onUnbind() called");
//        Preconditions.checkState(this.view != null, "Presenter is already unbound");
        viewDelegate.onUnbind();
    }

    @Override
    public void onDestroy() {
        // Do nothing
    }

    @NonNull
    public View getView() {
        return viewDelegateAsView;
    }
}
