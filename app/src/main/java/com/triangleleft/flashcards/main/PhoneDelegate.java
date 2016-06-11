package com.triangleleft.flashcards.main;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;
import com.triangleleft.flashcards.vocabular.VocabularListFragment;
import com.triangleleft.flashcards.vocabular.VocabularWordFragment;

import android.animation.ValueAnimator;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import butterknife.Bind;
import butterknife.ButterKnife;

/*package*/ class PhoneDelegate implements IMainActivityDelegate {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private final MainActivity activity;

    private final DrawerArrowDrawable arrowDrawable;
    private final ActionBarDrawerToggle toggle;
    private VocabularWordFragment vocabularWordFragment;
    private VocabularListFragment vocabularListFragment;

    public PhoneDelegate(MainActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        toggle = new ActionBarDrawerToggle(
                activity, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Do nothing, we don't want icon animation
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Do nothing, we don't want icon changing itself
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Do nothing, we don't want icon changing itself
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        arrowDrawable = (DrawerArrowDrawable) toolbar.getNavigationIcon();

        toggle.setToolbarNavigationClickListener(v -> onBackPressed());

        toggle.setHomeAsUpIndicator(arrowDrawable);
        toggle.setDrawerIndicatorEnabled(true);

        initPages();
    }

    @Override
    public void showList() {
        if (vocabularWordFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(vocabularWordFragment).commit();
        }

        if (vocabularListFragment == null) {
            vocabularListFragment = new VocabularListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, vocabularListFragment, VocabularListFragment.TAG)
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        getSupportFragmentManager().beginTransaction().show(vocabularListFragment).commit();

        setArrowIndicator(true);
    }

    @Override
    public void showWord(IVocabularWord word) {
        if (vocabularListFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(vocabularListFragment).commit();
        }

        if (vocabularWordFragment == null) {
            vocabularWordFragment = new VocabularWordFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, vocabularWordFragment, VocabularWordFragment.TAG)
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        vocabularWordFragment.getPresenter().setWord(word);
        getSupportFragmentManager().beginTransaction().show(vocabularWordFragment).commit();

        setArrowIndicator(false);
    }

    @Override
    public boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void onBackPressed() {
        activity.onBackPressed();
    }

    private void initPages() {
        // Try to get re-created fragments
        vocabularListFragment = (VocabularListFragment) getSupportFragmentManager()
                .findFragmentByTag(VocabularListFragment.TAG);
        vocabularWordFragment =
                (VocabularWordFragment) getSupportFragmentManager().findFragmentByTag(VocabularWordFragment.TAG);
    }

    private void setArrowIndicator(boolean visible) {
        if (toggle.isDrawerIndicatorEnabled() != visible) {
            toggle.setDrawerIndicatorEnabled(visible);
            playDrawerToggleAnim(!visible);
        }
    }

    private void playDrawerToggleAnim(boolean showArrow) {
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

    private FragmentManager getSupportFragmentManager() {
        return activity.getSupportFragmentManager();
    }
}
