package com.triangleleft.flashcards.main;

import com.google.common.base.Preconditions;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;
import com.triangleleft.flashcards.vocabular.VocabularListFragment;
import com.triangleleft.flashcards.vocabular.VocabularWordFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SlidingPaneLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TabletWideDelegate implements IMainActivityDelegate {

    @Bind(R.id.sliding_pane_layout)
    SlidingPaneLayout slidingPaneLayout;
    private final MainActivity activity;
    private VocabularWordFragment vocabularWordFragment;
    private VocabularListFragment vocabularListFragment;

    public TabletWideDelegate(MainActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        initPages();
    }

    @Override
    public void showList() {
        // List is already shown
    }

    @Override
    public void showWord(VocabularWord word) {
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
        vocabularListFragment = (VocabularListFragment) getSupportFragmentManager()
                .findFragmentByTag(VocabularListFragment.TAG);
        if (vocabularListFragment == null) {
            vocabularListFragment = new VocabularListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_container, vocabularListFragment, VocabularListFragment.TAG)
                    .commit();
        }

        vocabularWordFragment =
                (VocabularWordFragment) getSupportFragmentManager().findFragmentByTag(VocabularWordFragment.TAG);
        if (vocabularWordFragment == null) {
            vocabularWordFragment = new VocabularWordFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.secondary_container, vocabularWordFragment, VocabularWordFragment.TAG)
                    .commit();
        }
        getSupportFragmentManager().executePendingTransactions();
    }

    private FragmentManager getSupportFragmentManager() {
        return activity.getSupportFragmentManager();
    }
}
