package com.triangleleft.flashcards.ui.main;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.common.DrawableUtils;
import com.triangleleft.flashcards.ui.common.NavigationView;
import com.triangleleft.flashcards.ui.vocabular.VocabularyListFragment;
import com.triangleleft.flashcards.ui.vocabular.VocabularyWordFragment;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TabletWideDelegate implements IMainActivityDelegate {

    private final MainActivity activity;
    @Bind(R.id.sliding_pane_layout)
    SlidingPaneLayout slidingPaneLayout;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private VocabularyWordFragment vocabularyWordFragment;
    private VocabularyListFragment vocabularListFragment;

    public TabletWideDelegate(MainActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // As soon as we start dragging panel, reset scroll
                // Otherwise we could see some items being cut by clipping
                navigationView.resetScroll();
                navigationView.setAnimationProgress(slideOffset);
            }

            @Override
            public void onPanelOpened(View panel) {
                navigationView.setOnOverlayClickListener(null);
            }

            @Override
            public void onPanelClosed(View panel) {
                // When pannel is closed, clicking it should open it
                navigationView.setOnOverlayClickListener(view -> slidingPaneLayout.openPane());
            }
        });

        navigationView.setAnimationProgress(isDrawerOpen() ? 0f : 1f);

        Drawable drawable = DrawableUtils
            .getTintedDrawable(activity, R.drawable.ic_menu_black_24dp, R.color.textColorPrimary);
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
    public void showWord(Optional<VocabularyWord> word) {
        vocabularyWordFragment.getPresenter().showWord(word);
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

        vocabularyWordFragment =
            (VocabularyWordFragment) getSupportFragmentManager().findFragmentByTag(VocabularyWordFragment.TAG);
        if (vocabularyWordFragment == null) {
            vocabularyWordFragment = new VocabularyWordFragment();
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.secondary_container, vocabularyWordFragment, VocabularyWordFragment.TAG)
                .commitNow();
        } else {
            getSupportFragmentManager()
                .beginTransaction()
                .show(vocabularyWordFragment)
                .commitNow();
        }
    }

    private FragmentManager getSupportFragmentManager() {
        return activity.getSupportFragmentManager();
    }
}
