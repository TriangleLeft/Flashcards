package com.triangleleft.flashcards;

import com.triangleleft.flashcards.dagger.IComponent;
import com.triangleleft.flashcards.service.IVocabularWord;
import com.triangleleft.flashcards.ui.common.presenter.IPresenter;
import com.triangleleft.flashcards.ui.common.view.IView;
import com.triangleleft.flashcards.vocab.VocabularListFragment;
import com.triangleleft.flashcards.vocab.VocabularWordFragment;

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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private VocabularWordFragment wordFragment;

    public interface IBackPressable {
        void onBackPressed();
    }

    @Bind(R.id.main_container)
    RelativeLayout container;
    private ActionBarDrawerToggle toggle;
    private DrawerArrowDrawable arrowDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setHomeButtonEnabled(false);

        getSupportActionBar().setTitle("Haro!");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Disable hamburger icon animation during drawer open
                super.onDrawerSlide(drawerView, 0);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        arrowDrawable = (DrawerArrowDrawable) toolbar.getNavigationIcon();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toggle.setHomeAsUpIndicator(arrowDrawable);
        toggle.setDrawerIndicatorEnabled(true);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //showList();
    }

    @NonNull
    @Override
    protected IComponent buildComponent() {
        return null;
    }

    @NonNull
    @Override
    protected IPresenter getPresenter() {
        return null;
    }

    @NonNull
    @Override
    protected IView getView() {
        return null;
    }

    private void showList() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, new VocabularListFragment(), VocabularListFragment.TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                toggle.setDrawerIndicatorEnabled(true);
                playDrawerToggleAnim(arrowDrawable);
            }
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onWordSelected(View itemView, IVocabularWord word) {
        toggle.setDrawerIndicatorEnabled(false);
        playDrawerToggleAnim(arrowDrawable);

        wordFragment = new VocabularWordFragment();
        int[] screenLocation = new int[2];
        itemView.getLocationOnScreen(screenLocation);
        Bundle args = new Bundle();
        args.putParcelable(VocabularWordFragment.ARGUMENT_WORD, word);
        args.putIntArray(VocabularWordFragment.KEY_LOCATION, screenLocation);
        args.putInt(VocabularWordFragment.KEY_TOP, itemView.getTop());
        args.putInt(VocabularWordFragment.KEY_START_HEIGHT, itemView.getHeight());
        args.putInt(VocabularWordFragment.KEY_TARGET_HEIGHT, container.getHeight());
        wordFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, wordFragment, VocabularWordFragment.TAG)
                .addToBackStack(VocabularWordFragment.TAG).commit();
    }

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
