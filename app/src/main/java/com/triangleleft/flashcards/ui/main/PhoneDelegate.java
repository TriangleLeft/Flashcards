package com.triangleleft.flashcards.ui.main;

import android.animation.ValueAnimator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.annimon.stream.Optional;
import com.google.common.base.Preconditions;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.vocabular.VocabularyListFragment;
import com.triangleleft.flashcards.ui.vocabular.VocabularyWordFragment;

/*package*/ class PhoneDelegate implements IMainActivityDelegate {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.button_flashcards)
    FloatingActionButton fab;
    private final MainActivity activity;

    private final DrawerArrowDrawable arrowDrawable;
    private final ActionBarDrawerToggle toggle;
    private VocabularyWordFragment vocabularyWordFragment;
    private VocabularyListFragment vocabularListFragment;

    public PhoneDelegate(MainActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        toggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
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
        Preconditions.checkNotNull(arrowDrawable);
        arrowDrawable.setColor(ContextCompat.getColor(activity, R.color.textColorPrimary));

        toggle.setToolbarNavigationClickListener(v -> onBackPressed());

        toggle.setHomeAsUpIndicator(arrowDrawable);
        toggle.setDrawerIndicatorEnabled(true);

        initPages();
    }

    @Override
    public void showList() {
        hideFragment(vocabularyWordFragment);
        fab.show();

        if (vocabularListFragment == null) {
            vocabularListFragment = new VocabularyListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, vocabularListFragment, VocabularyListFragment.TAG)
                    .commitNow();
        } else {
            showFragment(vocabularListFragment);
        }

        setArrowIndicator(true);
    }

    @Override
    public void showWord(Optional<VocabularyWord> word) {
        if (!word.isPresent()) {
            // In phone view, just don't open this screen
            return;
        }
        hideFragment(vocabularListFragment);
        fab.hide();

        if (vocabularyWordFragment == null) {
            vocabularyWordFragment = new VocabularyWordFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, vocabularyWordFragment, VocabularyWordFragment.TAG)
                    .commitNow();
        } else {
            showFragment(vocabularyWordFragment);
        }
        vocabularyWordFragment.getPresenter().showWord(word);

        setArrowIndicator(false);
    }

    private void hideFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(fragment)
                    .commitNow();
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .show(fragment)
                .commitNow();
    }

    @Override
    public boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void reloadList() {
        showList();
        vocabularListFragment.getPresenter().onLoadList();
    }

    private void onBackPressed() {
        activity.onBackPressed();
    }

    private void initPages() {
        // Try to get re-created fragments
        vocabularListFragment = (VocabularyListFragment) getSupportFragmentManager()
                .findFragmentByTag(VocabularyListFragment.TAG);
        vocabularyWordFragment =
                (VocabularyWordFragment) getSupportFragmentManager().findFragmentByTag(VocabularyWordFragment.TAG);
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
