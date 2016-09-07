package com.triangleleft.flashcards.ui.main;

import com.android.debug.hv.ViewServer;
import com.annimon.stream.Optional;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.di.main.DaggerMainPageComponent;
import com.triangleleft.flashcards.di.main.MainPageComponent;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.cards.FlashcardsActivity;
import com.triangleleft.flashcards.ui.common.BaseActivity;
import com.triangleleft.flashcards.ui.common.NavigationView;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@FunctionsAreNonnullByDefault
public class MainActivity extends BaseActivity<MainPageComponent, IMainView, MainPresenter> implements IMainView {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        List<Integer> list = Arrays.asList(R.id.action_settings);
        for (Integer itemId : list) {
            MenuItem menuItem = menu.findItem(itemId);
            Drawable wrap = DrawableCompat.wrap(menuItem.getIcon());
            DrawableCompat.setTint(wrap, ContextCompat.getColor(this, R.color.textColorPrimary));
            menuItem.setIcon(wrap);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.action_settings:
            startActivity(new Intent(this, SettingsActivity.class));
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @NonNull
    @Override
    protected MainPageComponent buildComponent() {
        return DaggerMainPageComponent.builder()
                .androidApplicationComponent(getApplicationComponent())
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
    public void onFlashcardsClick() {
        Intent intent = new Intent(this, FlashcardsActivity.class);
        startActivity(intent);
    }
}
