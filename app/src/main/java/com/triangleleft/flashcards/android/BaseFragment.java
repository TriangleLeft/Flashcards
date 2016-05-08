package com.triangleleft.flashcards.android;

import com.triangleleft.flashcards.mvp.common.di.component.ApplicationComponent;
import com.triangleleft.flashcards.mvp.common.di.component.IComponent;
import com.triangleleft.flashcards.mvp.common.presenter.ComponentManager;
import com.triangleleft.flashcards.mvp.common.presenter.IPresenter;
import com.triangleleft.flashcards.mvp.common.view.IView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

public abstract class BaseFragment<Component extends IComponent, View extends IView,
        Presenter extends IPresenter<View>> extends Fragment {

    private static final Logger logger = LoggerFactory.getLogger(BaseFragment.class);
    private static final String KEY_COMPONENT_ID = "keyComponentId";

    ComponentManager componentManager;

    private Component component;

    @Inject
    Presenter presenter;

    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState);
        super.onCreate(savedInstanceState);

        boolean newComponent = true;
        componentManager = getApplicationComponent().componentManager();
        if (savedInstanceState == null) {
            component = buildComponent();
        } else {
            long presenterId = savedInstanceState.getLong(KEY_COMPONENT_ID);
            component = componentManager.restoreComponent(presenterId);
            if (component == null) {
                component = buildComponent();
            } else {
                newComponent = false;
            }
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
    public void onSaveInstanceState(Bundle outState) {
        logger.debug("onSaveInstanceState() called with: outState = [{}]", outState);
        super.onSaveInstanceState(outState);
        long componentId = componentManager.saveComponent(getComponent());
        outState.putLong(KEY_COMPONENT_ID, componentId);
    }

    @CallSuper
    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
        super.onDestroy();
        if (isRemoving() || !getActivity().isChangingConfigurations()) {
            getPresenter().onDestroy();
        }
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((FlashcardsApplication) getActivity().getApplication()).getComponent();
    }

    @NonNull
    protected Component getComponent() {
        return component;
    }

    @NonNull
    protected abstract Component buildComponent();

    @NonNull
    protected Presenter getPresenter() {
        return presenter;
    }

    @NonNull
    protected abstract View getMvpView();

}
