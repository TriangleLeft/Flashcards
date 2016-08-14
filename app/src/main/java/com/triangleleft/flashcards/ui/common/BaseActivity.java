package com.triangleleft.flashcards.ui.common;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.di.ApplicationComponent;
import com.triangleleft.flashcards.di.IComponent;
import com.triangleleft.flashcards.ui.common.presenter.IPresenter;
import com.triangleleft.flashcards.ui.common.view.IView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

public abstract class BaseActivity<Component extends IComponent, View extends IView,
        Presenter extends IPresenter<View>> extends AppCompatActivity {

    private static final Logger logger = LoggerFactory.getLogger(BaseActivity.class);

    private Component component;

    @Inject
    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState);
        super.onCreate(savedInstanceState);

        Optional<Component> optional = getApplicationComponent().componentManager().restoreComponent(getClass());
        this.component = optional.orElse(buildComponent());

        inject();
        if (!optional.isPresent()) {
            getPresenter().onCreate();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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
    protected void onDestroy() {
        logger.debug("onDestroy() called");
        super.onDestroy();
        if (!isChangingConfigurations()) {
            getPresenter().onDestroy();
        } else {
            getApplicationComponent().componentManager().saveComponent(getClass(), getComponent());
        }
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((FlashcardsApplication) getApplication()).getComponent();
    }

    public Component getComponent() {
        return component;
    }

    protected abstract Component buildComponent();

    public Presenter getPresenter() {
        return presenter;
    }

    protected abstract View getMvpView();
}
