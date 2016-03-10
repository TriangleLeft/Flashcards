package com.triangleleft.flashcards;

import com.triangleleft.flashcards.dagger.ApplicationComponent;
import com.triangleleft.flashcards.ui.common.presenter.IPresenter;
import com.triangleleft.flashcards.ui.common.presenter.IPresenterState;
import com.triangleleft.flashcards.ui.common.presenter.PresenterManager;
import com.triangleleft.flashcards.ui.common.view.IView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

public abstract class BaseActivity<View extends IView, State extends IPresenterState,
        Presenter extends IPresenter<View, State>> extends AppCompatActivity {

    private static final Logger logger = LoggerFactory.getLogger(BaseActivity.class);
    private static final String KEY_PRESENTER_ID = "keyPresenterId";

    @Inject
    PresenterManager presenterManager;

    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState);
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            presenter = buildPresenter();
        } else {
            long presenterId = savedInstanceState.getLong(KEY_PRESENTER_ID);
            presenter = presenterManager.restorePresenter(presenterId);
            if (presenter == null) {
                presenter = buildPresenter();
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
        long presenterId = presenterManager.savePresenter(getPresenter());
        outState.putLong(KEY_PRESENTER_ID, presenterId);
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

    protected abstract Presenter buildPresenter();

    protected Presenter getPresenter() {
        return presenter;
    }

    protected abstract View getView();

}
