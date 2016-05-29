package com.triangleleft.flashcards.main;

import com.google.common.base.Preconditions;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.squareup.picasso.Picasso;
import com.triangleleft.flashcards.BaseActivity;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.cards.FlashcardsActivity;
import com.triangleleft.flashcards.main.di.DaggerMainPageComponent;
import com.triangleleft.flashcards.main.di.MainPageComponent;
import com.triangleleft.flashcards.mvp.main.IMainView;
import com.triangleleft.flashcards.mvp.main.MainPageModule;
import com.triangleleft.flashcards.mvp.main.MainPresenter;
import com.triangleleft.flashcards.service.settings.ILanguage;
import com.triangleleft.flashcards.service.settings.IUserData;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;
import com.triangleleft.flashcards.vocabular.VocabularListFragment;
import com.triangleleft.flashcards.vocabular.VocabularWordFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPageComponent, IMainView, MainPresenter>
        implements IMainView {

    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    @Bind(R.id.main_container)
    ViewGroup container;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.drawer_list)
    RecyclerView recyclerView;
    @Bind(R.id.drawer_user_name)
    TextView drawerUserName;
    @Bind(R.id.drawer_user_avatar)
    ImageView drawerUserAvatar;

    private ActionBarDrawerToggle toggle;
    private DrawerArrowDrawable arrowDrawable;
    private VocabularWordFragment vocabularWordFragment;
    private VocabularListFragment vocabularListFragment;
    private List<ILanguage> languages;
    private DrawerLanguagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // TODO: create custom class
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
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

        adapter = new DrawerLanguagesAdapter((viewHolder, position) -> {
            getPresenter().onLanguageSelected(languages.get(position));
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // We should always show first vocabular list page
        initPages();
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
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, vocabularListFragment, VocabularListFragment.TAG)
                    .commit();
        }
        getSupportFragmentManager().beginTransaction().show(vocabularListFragment).commit();

        setArrowIndicator(true);
    }

    @Override
    public void showUserData(IUserData userData) {
        // TODO: split into several methods
        languages = Stream.of(userData.getLanguages()).filter(ILanguage::isLearning)
                .collect(Collectors.toList());
        ILanguage learningLanguage = Stream.of(languages).filter(ILanguage::isCurrentLearning).findFirst().get();

        Picasso.with(this).load(userData.getAvatar()).into(drawerUserAvatar);
        drawerUserName.setText(userData.getUsername());

        adapter.setData(languages);
        setCurrentLanguage(learningLanguage);
    }

    @Override
    public void setCurrentLanguage(ILanguage language) {
        getSupportActionBar().setTitle(language.getName());

        // TODO:
    }

    @Override
    public void showWord(@NonNull IVocabularWord word) {
        logger.debug("showWord() called with: word = [{}]", word);
        // TODO: we don't want to hide it on tablets, then again, I don't want to always check for bunch of flags
        // Maybe consider delegating all this stuff?
        if (vocabularListFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(vocabularListFragment).commit();
        }

        if (vocabularWordFragment == null) {
            vocabularWordFragment = new VocabularWordFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, vocabularWordFragment, VocabularWordFragment.TAG)
                    .commit();
        }
        vocabularWordFragment.getPresenter().setWord(word);
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

    @NonNull
    @Override
    public ActionBar getSupportActionBar() {
        ActionBar actionBar = super.getSupportActionBar();
        Preconditions.checkNotNull(actionBar);
        return actionBar;
    }

    @OnClick(R.id.button_flashcards)
    public void onFlashcardsClick() {
        Intent intent = new Intent(this, FlashcardsActivity.class);
        startActivity(intent);
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
