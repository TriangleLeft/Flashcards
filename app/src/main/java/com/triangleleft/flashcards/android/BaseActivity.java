package com.triangleleft.flashcards.android;

import com.triangleleft.flashcards.mvp.common.di.component.ApplicationComponent;
import com.triangleleft.flashcards.mvp.common.di.component.IComponent;
import com.triangleleft.flashcards.mvp.common.presenter.ComponentManager;
import com.triangleleft.flashcards.mvp.common.presenter.IPresenter;
import com.triangleleft.flashcards.mvp.common.view.IView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

public abstract class BaseActivity<Component extends IComponent, View extends IView,
        Presenter extends IPresenter<View>> extends AppCompatActivity {

    private static final Logger logger = LoggerFactory.getLogger(BaseActivity.class);
    private static final String KEY_COMPONENT_ID = "keyComponentId";

    ComponentManager componentManager;

    private Component component;

    @Inject
    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState);
        super.onCreate(savedInstanceState);

        componentManager = getApplicationComponent().componentManager();

        boolean newComponent = true;
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
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        getPresenter().onBind(getMvpView());
    }

    protected abstract void inject();

    @Override
    protected void onResume() {
        logger.debug("onResume() called");
        super.onResume();
        getPresenter().onRebind(getMvpView());
    }

    @Override
    protected void onPause() {
        logger.debug("onPause() called");
        super.onPause();
        getPresenter().onUnbind();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        logger.debug("onSaveInstanceState() called with: outState = [{}]", outState);
        super.onSaveInstanceState(outState);
        long componentId = componentManager.saveComponent(getComponent());
        outState.putLong(KEY_COMPONENT_ID, componentId);
    }

    @Override
    protected void onDestroy() {
        logger.debug("onDestroy() called");
        super.onDestroy();
        if (!isChangingConfigurations()) {
            getPresenter().onDestroy();
        }
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((FlashcardsApplication) getApplication()).getComponent();
    }

    @NonNull
    public Component getComponent() {
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
