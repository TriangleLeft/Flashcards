package com.triangleleft.flashcards.ui.common.presenter;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.triangleleft.flashcards.ui.ViewEvent;
import com.triangleleft.flashcards.ui.ViewState;
import com.triangleleft.flashcards.ui.common.view.IView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPresenter<View extends IView<VS, VE>, VS extends ViewState, VE extends ViewEvent>
        implements IPresenter<View, VS> {

    public static final String VIEW_EXECUTOR = "viewExecutor";
    public static final String UI_SCHEDULER = "uiScheduler";

    private static final Logger logger = LoggerFactory.getLogger(AbstractPresenter.class);
    private volatile View view;

    public AbstractPresenter() {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onCreate(@Nullable VS savedViewState) {

    }

    @Override
    public VS getInstanceViewState() {
        return null;
    }

    @Override
    public void onDestroy() {

    }

    @UiThread
    @CallSuper
    @Override
    public void onBind(View view) {
        logger.debug("onBind() called with: view = [{}]", view);
        this.view = view;
    }

    @UiThread
    @CallSuper
    @Override
    public void onRebind(View view) {
        logger.debug("onRebind() called with: view = [{}]", view);
        this.view = view;
    }

    @UiThread
    @CallSuper
    @Override
    public void onUnbind() {
        logger.debug("onUnbind() called");
        view = null;
    }

    @Override
    public View getView() {
        return view;
    }
}
