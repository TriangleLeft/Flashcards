package com.triangleleft.flashcards.ui.common;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.triangleleft.flashcards.ui.common.di.ApplicationComponent;
import com.triangleleft.flashcards.ui.common.di.component.IComponent;
import com.triangleleft.flashcards.ui.common.presenter.IPresenter;
import com.triangleleft.flashcards.ui.common.view.IView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public abstract class BaseFragment<Component extends IComponent, View extends IView,
        Presenter extends IPresenter<View>> extends Fragment {

    private static final Logger logger = LoggerFactory.getLogger(BaseFragment.class);

    private Component component;

    @Inject
    Presenter presenter;

    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState);
        super.onCreate(savedInstanceState);

        boolean newComponent = true;
        this.component = getApplicationComponent().componentManager().restoreComponent(getClass());
        if (this.component == null) {
            this.component = buildComponent();
        } else {
            newComponent = false;
        }

        inject();
        if (newComponent) {
            getPresenter().onCreate();
        }
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
        if (isRemoving() || !getActivity().isChangingConfigurations()) {
            getPresenter().onDestroy();
        } else {
            getApplicationComponent().componentManager().saveComponent(getClass(), getComponent());
        }
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((FlashcardsApplication) getActivity().getApplication()).getComponent();
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
