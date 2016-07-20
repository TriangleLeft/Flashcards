package com.triangleleft.flashcards.ui.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.annimon.stream.Optional;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.cards.FlashcardsActivity;
import com.triangleleft.flashcards.ui.common.BaseActivity;
import com.triangleleft.flashcards.ui.common.FlagImagesProvider;
import com.triangleleft.flashcards.ui.common.NavigationView;
import com.triangleleft.flashcards.ui.main.di.DaggerMainPageComponent;
import com.triangleleft.flashcards.ui.main.di.MainPageComponent;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

@FunctionsAreNonnullByDefault
public class MainActivity extends BaseActivity<MainPageComponent, IMainView, MainPresenter> implements IMainView {

    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);
    private static final String FRAGMENTS_TAG = "android:support:fragments";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    @Inject
    FlagImagesProvider flagImagesProvider;

    private DrawerLanguagesAdapter adapter;
    private IMainActivityDelegate delegate;
    private Handler handler;

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
        logger.debug("onCreate() width: {}, fixedDrawer: {}, twoPanes: {}", screenWidthDp, fixedDrawer, twoPanes);

        // TODO: Factory.buildViewDelegate(fixedDrawer, twoPanes);
        if (fixedDrawer && twoPanes) {
            delegate = new TabletWideDelegate(this);
        } else if (fixedDrawer) {
            delegate = new PhoneDelegate(this);
        } else {
            delegate = new PhoneDelegate(this);
        }

        adapter = new DrawerLanguagesAdapter(flagImagesProvider,
            (viewHolder, position) -> getPresenter().onLanguageSelected(adapter.getItem(position)));
        navigationView.setLanguagesAdapter(adapter);

        handler = new Handler();
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
        return DaggerMainPageComponent.builder().applicationComponent(getApplicationComponent())
            .mainPageModule(new MainPageModule()).build();
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
    public void showUserData(Optional<String> username, Optional<String> avatar, List<Language> languages) {
        logger.debug("showUserData()");
        // We assume that first language in list is the one we are learning
        // Though it's possible that we don't learn any languages
        if (languages.size() > 0) {
            toolbar.setTitle(languages.get(0).getName());
        } else {
            toolbar.setTitle(R.string.app_name);
        }

        navigationView.showUserData(username, avatar);
        handler.post(() -> adapter.setData(languages));
    }

    @Override
    public void reloadList() {
        logger.debug("reloadList()");
        delegate.reloadList();
    }

    @Override
    public void showDrawerProgress() {
        navigationView.showDrawerProgress();
    }

    @Override
    public void showDrawerError() {
        // TODO: show drawer error
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
