package com.triangleleft.flashcards.android;

import com.triangleleft.flashcards.dagger.component.ApplicationComponent;
import com.triangleleft.flashcards.dagger.component.IComponent;
import com.triangleleft.flashcards.mvp.common.presenter.ComponentManager;
import com.triangleleft.flashcards.mvp.common.presenter.IPresenter;
import com.triangleleft.flashcards.mvp.common.view.IView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        logger.debug("onActivityCreated() called with: savedInstanceState = [{}]", savedInstanceState);
        super.onActivityCreated(savedInstanceState);

        componentManager = getApplicationComponent().componentManager();

        if (savedInstanceState == null) {
            component = buildComponent();
        } else {
            long presenterId = savedInstanceState.getLong(KEY_COMPONENT_ID);
            component = componentManager.restoreComponent(presenterId);
            if (component == null) {
                component = buildComponent();
            }
        }

        inject();
    }

    protected abstract void inject();

    @Override
    public void onResume() {
        logger.debug("onResume() called");
        super.onResume();
        getPresenter().onBind(getMvpView());
    }

    @Override
    public void onPause() {
        logger.debug("onPause() called");
        super.onPause();
        getPresenter().onUnbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        logger.debug("onSaveInstanceState() called with: outState = [{}]", outState);
        super.onSaveInstanceState(outState);
        long componentId = componentManager.saveComponent(getComponent());
        outState.putLong(KEY_COMPONENT_ID, componentId);
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
        super.onDestroy();
        getPresenter().onDestroy();
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
