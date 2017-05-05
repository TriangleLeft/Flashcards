package com.triangleleft.flashcards.ui.common;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.di.ApplicationComponent;
import com.triangleleft.flashcards.di.IComponent;
import com.triangleleft.flashcards.ui.FlashcardsApplication;
import com.triangleleft.flashcards.ui.ViewState;
import com.triangleleft.flashcards.ui.common.presenter.IPresenter;
import com.triangleleft.flashcards.ui.common.view.IView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public abstract class BaseFragment<Component extends IComponent, View extends IView, VS extends ViewState,
        Presenter extends IPresenter<View, VS>> extends Fragment {

    private static final Logger logger = LoggerFactory.getLogger(BaseFragment.class);
    @Inject
    Presenter presenter;
    private Component component;

    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState);

        Optional<Component> restoredComponent =
                getApplicationComponent().componentManager().restoreComponent(getClass());
        this.component = restoredComponent.orElseGet(this::buildComponent);

        inject();
        if (!restoredComponent.isPresent()) {
            VS viewState = null;
            if (savedInstanceState != null) {
                viewState = (VS) savedInstanceState.getSerializable(getClass().getSimpleName());
            }
            getPresenter().onCreate(viewState);
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getClass().getSimpleName(), getPresenter().getInstanceViewState());
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().onBind(getMvpView());
    }

    protected abstract void inject();

    @CallSuper
    @Override
    public void onResume() {
        logger.debug("onResume() called");
        super.onResume();
        getPresenter().onRebind(getMvpView());
    }

    @CallSuper
    @Override
    public void onPause() {
        logger.debug("onPause() called");
        super.onPause();
        getPresenter().onUnbind();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
        super.onDestroy();
        getFlashcardsApplication().getRefWatcher().watch(this);
        if (isRemoving() || !getActivity().isChangingConfigurations()) {
            // We are leaving this screen, notify presenter
            getPresenter().onDestroy();
            // Component and presenter should be GCed
            getFlashcardsApplication().getRefWatcher().watch(getComponent());
            getFlashcardsApplication().getRefWatcher().watch(getPresenter());
        } else {
            getApplicationComponent().componentManager().saveComponent(getClass(), getComponent());
        }
    }

    protected FlashcardsApplication getFlashcardsApplication() {
        return (FlashcardsApplication) getActivity().getApplication();
    }

    protected ApplicationComponent getApplicationComponent() {
        return getFlashcardsApplication().getComponent();
    }

    protected Component getComponent() {
        return component;
    }

    protected abstract Component buildComponent();

    public Presenter getPresenter() {
        return presenter;
    }

    protected abstract View getMvpView();
}
