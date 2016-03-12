package com.triangleleft.flashcards;

import com.triangleleft.flashcards.dagger.ApplicationComponent;
import com.triangleleft.flashcards.dagger.IComponent;
import com.triangleleft.flashcards.ui.common.presenter.ComponentManager;
import com.triangleleft.flashcards.ui.common.presenter.IPresenter;
import com.triangleleft.flashcards.ui.common.view.IView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<Component extends IComponent, View extends IView,
        Presenter extends IPresenter<View>> extends AppCompatActivity {

    private static final Logger logger = LoggerFactory.getLogger(BaseActivity.class);
    private static final String KEY_COMPONENT_ID = "keyComponentId";

    ComponentManager componentManager;

    private Component component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState);
        super.onCreate(savedInstanceState);

        componentManager = getApplicationComponent().componentManager();

        if (savedInstanceState == null) {
            component = buildComponent();
        } else {
            long presenterId = savedInstanceState.getLong(KEY_COMPONENT_ID);
            component = componentManager.restorePresenter(presenterId);
            if (component == null) {
                component = buildComponent();
            }
        }
    }

    @Override
    protected void onResume() {
        logger.debug("onResume() called");
        super.onResume();
        getPresenter().onBind(getView());
    }

    @Override
    protected void onPause() {
        logger.debug("onPause() called");
        super.onPause();
        getPresenter().onUnbind(getView());
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
        getPresenter().onDestroy();
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((FlashcardsApplication) getApplication()).getComponent();
    }

    @NonNull
    protected Component getComponent() {
        return component;
    }

    @NonNull
    protected abstract Component buildComponent();

    @NonNull
    protected abstract Presenter getPresenter();

    @NonNull
    protected abstract View getView();

}
