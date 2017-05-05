package com.triangleleft.flashcards.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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

public abstract class BaseActivity<Component extends IComponent, View extends IView, VS extends ViewState,
        Presenter extends IPresenter<View, VS>> extends AppCompatActivity {

    private static final Logger logger = LoggerFactory.getLogger(BaseActivity.class.getSimpleName());

    private Component component;

    @Inject
    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState);

        Optional<Component> restoredComponent =
                getApplicationComponent().componentManager().restoreComponent(getClass());
        this.component = restoredComponent.orElseGet(this::buildComponent);

        inject();
        // If we didn't had restored component, it's a new one, run presenter's onCreate
        // So... if it's first launch after process death, we have no presenter, but we could have saved instance state
        if (!restoredComponent.isPresent()) {
            VS viewState = null;
            if (savedInstanceState != null) {
                viewState = (VS) savedInstanceState.getSerializable(getClass().getSimpleName());
            }
            getPresenter().onCreate(viewState);
        }

        // We want components/presenters to be created before fragments are instantiated
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // TODO: Remove this
        getPresenter().onBind(getMvpView());
    }

    protected abstract void inject();

    @Override
    protected void onStart() {
        logger.debug("onStart() called");
        super.onStart();
        getPresenter().onRebind(getMvpView());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getClass().getSimpleName(), getPresenter().getInstanceViewState());
    }

    @Override
    protected void onStop() {
        logger.debug("onStop() called");
        super.onStop();
        getPresenter().onUnbind();
    }

    @Override
    protected void onDestroy() {
        logger.debug("onDestroy() called");
        super.onDestroy();
        // Activity instance should be GCed
        getFlashcardsApplication().getRefWatcher().watch(this);
        if (!isChangingConfigurations()) {
            // We are leaving this screen, notify presenter
            getPresenter().onDestroy();
            // Component and presenter should be GCed
            getFlashcardsApplication().getRefWatcher().watch(getComponent());
            getFlashcardsApplication().getRefWatcher().watch(getPresenter());
        } else {
            getApplicationComponent().componentManager().saveComponent(getClass(), getComponent());
        }
    }

    protected final FlashcardsApplication getFlashcardsApplication() {
        return (FlashcardsApplication) getApplication();
    }

    protected ApplicationComponent getApplicationComponent() {
        return getFlashcardsApplication().getComponent();
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
