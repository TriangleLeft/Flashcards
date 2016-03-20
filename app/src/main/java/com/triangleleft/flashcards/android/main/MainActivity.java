package com.triangleleft.flashcards.android.main;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.android.BaseActivity;
import com.triangleleft.flashcards.android.vocabular.VocabularListFragment;
import com.triangleleft.flashcards.android.vocabular.VocabularWordFragment;
import com.triangleleft.flashcards.mvp.main.di.MainPageComponent;
import com.triangleleft.flashcards.mvp.main.presenter.IMainPresenter;
import com.triangleleft.flashcards.mvp.main.view.IMainView;
import com.triangleleft.flashcards.mvp.main.view.MainViewPage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<MainPageComponent, IMainView, IMainPresenter>
        implements IMainView, NavigationView.OnNavigationItemSelectedListener {

    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    private VocabularListFragment vocabularListFragment;
    private VocabularWordFragment vocabularWordFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Haro!");

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Disable hamburger icon animation during drawer open
                super.onDrawerSlide(drawerView, 0);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        arrowDrawable = (DrawerArrowDrawable) toolbar.getNavigationIcon();

        toggle.setToolbarNavigationClickListener(v -> onBackPressed());

        toggle.setHomeAsUpIndicator(arrowDrawable);
        toggle.setDrawerIndicatorEnabled(true);


        navigationView.setNavigationItemSelectedListener(this);

        // We should always show first vocabular list page
        initPages();
    }

    private void initPages() {
        vocabularListFragment = new VocabularListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, vocabularListFragment, null)
                .commit();

        vocabularWordFragment = new VocabularWordFragment();
        // Hide word fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, vocabularWordFragment, null)
                .detach(vocabularWordFragment)
                .commit();
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @NonNull
    @Override
    protected MainPageComponent buildComponent() {
        return getApplicationComponent().getApplication().buildMainActivityComponent();
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
    public void setPage(@NonNull MainViewPage state) {
        logger.debug("setPage() called with: state = [{}]", state);
        switch (state) {
            case LIST:
                getSupportFragmentManager().beginTransaction()
                        .detach(vocabularWordFragment)
                        .commit();
                break;
            case WORD:
                getSupportFragmentManager().beginTransaction()
                        .attach(vocabularWordFragment)
                        .commit();
                break;
            default:
                throw new IllegalStateException("Unknown state " + state.name());
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                toggle.setDrawerIndicatorEnabled(true);
                playDrawerToggleAnim(arrowDrawable);
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//    public void onWordSelected(View itemView, IVocabularWord word) {
//        toggle.setDrawerIndicatorEnabled(false);
//        playDrawerToggleAnim(arrowDrawable);
//
//        wordFragment = new VocabularWordFragment();
//        int[] screenLocation = new int[2];
//        itemView.getLocationOnScreen(screenLocation);
//        Bundle args = new Bundle();
//        args.putParcelable(VocabularWordFragment.ARGUMENT_WORD, word);
//        args.putIntArray(VocabularWordFragment.KEY_LOCATION, screenLocation);
//        args.putInt(VocabularWordFragment.KEY_TOP, itemView.getTop());
//        args.putInt(VocabularWordFragment.KEY_START_HEIGHT, itemView.getHeight());
//        args.putInt(VocabularWordFragment.KEY_TARGET_HEIGHT, container.getHeight());
//        wordFragment.setArguments(args);
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.main_container, wordFragment, VocabularWordFragment.TAG)
//                .addToBackStack(VocabularWordFragment.TAG).commit();
//    }

    public static void playDrawerToggleAnim(final DrawerArrowDrawable d) {
        float start = d.getProgress();
        float end = Math.abs(start - 1);
        ValueAnimator offsetAnimator = ValueAnimator.ofFloat(start, end);
        offsetAnimator.setDuration(300);
        offsetAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        offsetAnimator.addUpdateListener(animation -> {
            float offset = (Float) animation.getAnimatedValue();
            d.setProgress(offset);
        });
        offsetAnimator.start();
    }
}
