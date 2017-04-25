package com.triangleleft.flashcards.ui.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.debug.hv.ViewServer;
import com.annimon.stream.Optional;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.di.main.DaggerMainPageComponent;
import com.triangleleft.flashcards.di.main.MainPageComponent;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.cards.FlashcardsActivity;
import com.triangleleft.flashcards.ui.common.BaseActivity;
import com.triangleleft.flashcards.ui.login.ViewState;
import com.triangleleft.flashcards.ui.main.drawer.NavigationView;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@FunctionsAreNonnullByDefault
public class MainActivity extends BaseActivity<MainPageComponent, IMainView, ViewState, MainPresenter> implements IMainView {

    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);
    private static final String FRAGMENTS_TAG = "android:support:fragments";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.main_container)
    View mainContainer;

    private IMainActivityDelegate delegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // We don't want fragments to be recreated automatically
            savedInstanceState.putParcelable(FRAGMENTS_TAG, null);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        boolean fixedDrawer = getResources().getBoolean(R.bool.fixed_drawer);
        boolean twoPanes = getResources().getBoolean(R.bool.two_panes);

        Configuration configuration = getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;
        int screenHeightDp = configuration.screenHeightDp;
        logger.debug("onCreate() width: {}, height: {}", screenWidthDp, screenHeightDp);

        // TODO: Factory.buildViewDelegate(fixedDrawer, twoPanes);
        if (fixedDrawer && twoPanes) {
            delegate = new TabletWideDelegate(this);
        } else if (fixedDrawer) {
            delegate = new PhoneDelegate(this);
        } else {
            delegate = new PhoneDelegate(this);
        }

        navigationView.init(getComponent());
        ViewServer.get(this).addWindow(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @NonNull
    @Override
    protected MainPageComponent buildComponent() {
        return DaggerMainPageComponent.builder()
                .applicationComponent(getApplicationComponent())
                .mainPageModule(new MainPageModule())
                .build();
    }

    @NonNull
    @Override
    protected IMainView getMvpView() {
        return this;
    }

    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void showList() {
        logger.debug("showList() called");
        delegate.showList();
    }

    @Override
    public void reloadList() {
        logger.debug("reloadList()");
        delegate.reloadList();
    }

    @Override
    public void showFlashcardsDialog() {
        // Create custom dialog class with presenter?
        FlashcardSettingsDialog settingsDialog = new FlashcardSettingsDialog(this, (dialog, which) -> {
            switch (which) {
                case FlashcardSettingsDialog.BUTTON_POSITIVE:
                    Intent intent = new Intent(this, FlashcardsActivity.class);
                    startActivity(intent);
                    break;
                default:
                    dialog.dismiss();
                    getPresenter().setSettingsDialogShown(false);
            }
        });
        settingsDialog.show();
    }

    @Override
    public void showWord(@NonNull Optional<VocabularyWord> word) {
        logger.debug("showWord() called with: word = [{}]", word);
        delegate.showWord(word);
    }

    @Override
    public void onBackPressed() {
        if (delegate.isDrawerOpen()) {
            delegate.closeDrawer();
        } else {
            getPresenter().onBackPressed();
        }
    }

    @OnClick(R.id.button_flashcards)
    public void onFlashcardsClick(View view) {
        showFlashcardsDialog();
        getPresenter().setSettingsDialogShown(true);
//        Intent intent = new Intent(this, FlashcardsActivity.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            int[] location = new int[2];
//            view.getLocationOnScreen(location);
//            int cx = location[0] + view.getWidth() / 2;
//            int cy = location[1] + view.getHeight() / 2;
//            intent.putExtra(FlashcardsActivity.REVEAL_X, cx);
//            intent.putExtra(FlashcardsActivity.REVEAL_Y, cy);
//            startActivity(intent);
//            overridePendingTransition(-1, -1);
//        } else {
//            startActivity(intent);
//        }
    }
}
