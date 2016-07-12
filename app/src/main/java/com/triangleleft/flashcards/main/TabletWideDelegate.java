package com.triangleleft.flashcards.main;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.common.base.Preconditions;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.common.NavigationView;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.vocabular.VocabularWordFragment;
import com.triangleleft.flashcards.vocabular.VocabularyListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TabletWideDelegate implements IMainActivityDelegate {

    @Bind(R.id.sliding_pane_layout)
    SlidingPaneLayout slidingPaneLayout;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private final MainActivity activity;
    private VocabularWordFragment vocabularWordFragment;
    private VocabularyListFragment vocabularListFragment;

    public TabletWideDelegate(MainActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                navigationView.setAnimationProgress(slideOffset);
            }

            @Override
            public void onPanelOpened(View panel) {
                navigationView.setOnOverlayClickListener(null);
            }

            @Override
            public void onPanelClosed(View panel) {
                navigationView.setOnOverlayClickListener(view -> slidingPaneLayout.openPane());
            }
        });

        navigationView.setAnimationProgress(isDrawerOpen() ? 0f : 1f);

        Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(activity, R.drawable.ic_menu_black_24dp));
        DrawableCompat.setTint(drawable, ContextCompat.getColor(activity, R.color.white));
        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(v -> {
            if (slidingPaneLayout.isOpen()) {
                slidingPaneLayout.closePane();
            } else {
                slidingPaneLayout.openPane();
            }
        });

        initPages();
    }

    @Override
    public void showList() {
        // List is already shown
    }

    @Override
    public void showWord(VocabularyWord word) {
        Preconditions.checkNotNull(vocabularWordFragment, "Vocabular word fragment was not bound!");
        vocabularWordFragment.getPresenter().setWord(word);
    }

    @Override
    public boolean isDrawerOpen() {
        return slidingPaneLayout.isOpen();
    }

    @Override
    public void closeDrawer() {
        slidingPaneLayout.closePane();
    }

    @Override
    public void reloadList() {
        vocabularListFragment.getPresenter().onLoadList();
    }

    private void initPages() {
        // Try to get re-created fragments
        vocabularListFragment = (VocabularyListFragment) getSupportFragmentManager()
                .findFragmentByTag(VocabularyListFragment.TAG);
        if (vocabularListFragment == null) {
            vocabularListFragment = new VocabularyListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_container, vocabularListFragment, VocabularyListFragment.TAG)
                    .commitNow();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(vocabularListFragment)
                    .commitNow();
        }

        vocabularWordFragment =
                (VocabularWordFragment) getSupportFragmentManager().findFragmentByTag(VocabularWordFragment.TAG);
        if (vocabularWordFragment == null) {
            vocabularWordFragment = new VocabularWordFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.secondary_container, vocabularWordFragment, VocabularWordFragment.TAG)
                    .commitNow();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(vocabularWordFragment)
                    .commitNow();
        }
    }

    private FragmentManager getSupportFragmentManager() {
        return activity.getSupportFragmentManager();
    }
}
