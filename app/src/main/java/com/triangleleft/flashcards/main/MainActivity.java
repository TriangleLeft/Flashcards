package com.triangleleft.flashcards.main;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.BaseActivity;
import com.triangleleft.flashcards.common.FlashcardsApplication;
import com.triangleleft.flashcards.cards.FlashcardsActivity;
import com.triangleleft.flashcards.vocabular.VocabularListFragment;
import com.triangleleft.flashcards.vocabular.VocabularWordFragment;
import com.triangleleft.flashcards.mvp.main.DaggerMainPageComponent;
import com.triangleleft.flashcards.mvp.main.IMainView;
import com.triangleleft.flashcards.main.di.MainPageComponent;
import com.triangleleft.flashcards.mvp.main.MainPageModule;
import com.triangleleft.flashcards.mvp.main.MainPresenter;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<MainPageComponent, IMainView, MainPresenter>
        implements IMainView, NavigationView.OnNavigationItemSelectedListener {

    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);


    @Bind(R.id.main_container)
    ViewGroup container;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private DrawerArrowDrawable arrowDrawable;
    private VocabularWordFragment vocabularWordFragment;
    private VocabularListFragment vocabularListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);


        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Do nothing, we are animating arrow by ourselves
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        arrowDrawable = (DrawerArrowDrawable) toolbar.getNavigationIcon();

        toggle.setToolbarNavigationClickListener(v -> onBackPressed());

        toggle.setHomeAsUpIndicator(arrowDrawable);
        toggle.setDrawerIndicatorEnabled(true);

        toolbar.setTitle("Haro!");
        getSupportActionBar().setTitle("Orly?");


        navigationView.setNavigationItemSelectedListener(this);

        // We should always show first vocabular list page
        initPages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        Drawable drawable = menu.findItem(R.id.action_flashcards).getIcon();

        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.textColorPrimary));
        menu.findItem(R.id.action_flashcards).setIcon(drawable);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FlashcardsApplication.showDebugToast(item.getTitle().toString());
        Intent intent = new Intent(this, FlashcardsActivity.class);
        startActivity(intent);
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

    private void initPages() {
        // Try to get re-created fragments
        vocabularListFragment = (VocabularListFragment) getSupportFragmentManager()
                .findFragmentByTag(VocabularListFragment.TAG);
        vocabularWordFragment =
                (VocabularWordFragment) getSupportFragmentManager().findFragmentByTag(VocabularWordFragment.TAG);
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
        if (vocabularWordFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(vocabularWordFragment).commit();
        }

        if (vocabularListFragment == null) {
            vocabularListFragment = new VocabularListFragment();
            vocabularListFragment.setArguments(new Bundle());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, vocabularListFragment, VocabularListFragment.TAG)
                    .commit();
        }
        getSupportFragmentManager().beginTransaction().show(vocabularListFragment).commit();

        setArrowIndicator(true);
    }

    @Override
    public void showWord(@NonNull IVocabularWord word) {
        logger.debug("showWord() called with: word = [{}]", word);
        if (vocabularListFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(vocabularListFragment).commit();
        }

        if (vocabularWordFragment == null) {
            vocabularWordFragment = new VocabularWordFragment();
            vocabularWordFragment.setArguments(new Bundle());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, vocabularWordFragment, VocabularWordFragment.TAG)
                    .commit();
        }
        vocabularWordFragment.getArguments().putParcelable(VocabularWordFragment.KEY_WORD, word);
        getSupportFragmentManager().beginTransaction().show(vocabularWordFragment).commit();

        setArrowIndicator(false);
    }

    private void setArrowIndicator(boolean visible) {
        if (toggle.isDrawerIndicatorEnabled() != visible) {
            toggle.setDrawerIndicatorEnabled(visible);
            playDrawerToggleAnim(!visible);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            getPresenter().onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void playDrawerToggleAnim(boolean showArrow) {
        float start = arrowDrawable.getProgress();
        float end = showArrow ? 1 : 0;
        ValueAnimator offsetAnimator = ValueAnimator.ofFloat(start, end);
        offsetAnimator.setDuration(300);
        offsetAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        offsetAnimator.addUpdateListener(animation -> {
            float offset = (Float) animation.getAnimatedValue();
            arrowDrawable.setProgress(offset);
        });
        offsetAnimator.start();
    }
}
