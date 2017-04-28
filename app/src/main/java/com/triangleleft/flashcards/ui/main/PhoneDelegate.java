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

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.vocabular.VocabularyListFragment;
import com.triangleleft.flashcards.ui.vocabular.VocabularyWordFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/*package*/ class PhoneDelegate implements IMainActivityDelegate {

    private final MainActivity activity;
    private final DrawerArrowDrawable arrowDrawable;
    private final ActionBarDrawerToggle toggle;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.button_flashcards)
    FloatingActionButton fab;
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
        }
        if (!vocabularListFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, vocabularListFragment, VocabularyListFragment.Companion.getTAG())
                    .commitNow();
        }

        showFragment(vocabularListFragment);

        setArrowIndicator(true);
    }

    @Override
    public void showWord(Optional<VocabularyWord> word) {
        if (!word.isPresent()) {
            // In phone view, just don't open this screen
            // FIXME: when we turn screen from tablet to phone, current state is word
            // But when we have no words, we don't show anything
            // Either also show "no data" here and remove return
            // Or make sure that if we are moving from tablet to phone and and there are no words, show list
            // Ugh...
            return;
        }
        hideFragment(vocabularListFragment);
        fab.hide();

        if (vocabularyWordFragment == null) {
            vocabularyWordFragment = new VocabularyWordFragment();
        }
        if (!vocabularyWordFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, vocabularyWordFragment, VocabularyWordFragment.TAG)
                    .commitNow();
        }
        showFragment(vocabularyWordFragment);

        vocabularyWordFragment.getPresenter().showWord(word);

        setArrowIndicator(false);
    }

    private void initPages() {
        // Check whether we've got some fragments saved
        // NOTE: we actually don't know whether they are attached to proper parents
        // TODO: we can check their ids? or use reflection to get mContainerId (can we also use it to change it?)
        vocabularListFragment = (VocabularyListFragment) getSupportFragmentManager()
                .findFragmentByTag(VocabularyListFragment.Companion.getTAG());
        // List can be attached only to main_container
        // If needed we would show it later
        if (vocabularListFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(vocabularListFragment).commitNow();
        }

        vocabularyWordFragment =
                (VocabularyWordFragment) getSupportFragmentManager().findFragmentByTag(VocabularyWordFragment.TAG);
        if (vocabularyWordFragment != null) {
            // Word could be attached to another container, so we have to remove it
            getSupportFragmentManager().beginTransaction().remove(vocabularyWordFragment).commitNow();
        }
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
