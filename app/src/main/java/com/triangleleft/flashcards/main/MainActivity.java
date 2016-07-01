package com.triangleleft.flashcards.main;

import com.squareup.picasso.Picasso;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.cards.FlashcardsActivity;
import com.triangleleft.flashcards.common.BaseActivity;
import com.triangleleft.flashcards.common.FlagImagesProvider;
import com.triangleleft.flashcards.main.di.DaggerMainPageComponent;
import com.triangleleft.flashcards.main.di.MainPageComponent;
import com.triangleleft.flashcards.mvp.main.IMainView;
import com.triangleleft.flashcards.mvp.main.MainPageModule;
import com.triangleleft.flashcards.mvp.main.MainPresenter;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@FunctionsAreNonnullByDefault
public class MainActivity extends BaseActivity<MainPageComponent, IMainView, MainPresenter> implements IMainView {

    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);


    private static final String FRAGMENTS_TAG = "android:support:fragments";
    private static final int DRAWER_PAGE_PROGRESS = 0;
    private static final int DRAWER_PAGE_CONTENT = 1;

    @Bind(R.id.drawer_list)
    RecyclerView recyclerView;
    @Bind(R.id.drawer_user_name)
    TextView drawerUserName;
    @Bind(R.id.drawer_user_avatar)
    ImageView drawerUserAvatar;
    @Bind(R.id.drawer_content_flipper)
    ViewFlipper drawerContentFlipper;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    FlagImagesProvider flagImagesProvider;

    private DrawerLanguagesAdapter adapter;
    private IMainActivityDelegate delegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // We don't want fragments to be recreated automatically
            savedInstanceState.putParcelable(FRAGMENTS_TAG, null);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        boolean fixedDrawer = getResources().getBoolean(R.bool.fixed_drawer);
        boolean twoPanes = getResources().getBoolean(R.bool.two_panes);

        Configuration configuration = getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;
        logger.debug("onCreate() width: {}, fixedDrawer: {}, twoPanes: {}", screenWidthDp, fixedDrawer, twoPanes);

        // TODO: Factory.buildViewDelegate(fixedDrawer, twoPanes);
        if (fixedDrawer && twoPanes) {
            delegate = new TabletWideDelegate(this);
        } else if (fixedDrawer) {
            delegate = new PhoneDelegate(this);
        } else {
            delegate = new PhoneDelegate(this);
        }

        adapter = new DrawerLanguagesAdapter(flagImagesProvider,
                (viewHolder, position) -> getPresenter().onLanguageSelected(adapter.getItem(position)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
        delegate.showList();
    }

    @Override
    public void showUserData(String username, String avatar, List<Language> languages) {
        logger.debug("showUserData() called with: username = [{}], avatar = [{}]", username, avatar);
        Picasso.with(this).load(avatar).into(drawerUserAvatar);
        drawerUserName.setText(username);

        // We assume that first language in list is the one we are learning
        // Though it's possible that we don't learn any languages
        if (languages.size() > 0) {
            toolbar.setTitle(languages.get(0).getName());
        } else {
            toolbar.setTitle(R.string.app_name);
        }
        drawerContentFlipper.setDisplayedChild(DRAWER_PAGE_CONTENT);
        adapter.setData(languages);
        // FIXME: this one is called when we rotate activity
        delegate.reloadList();
    }

    @Override
    public void showDrawerProgress() {
        //drawerContentFlipper.setDisplayedChild(DRAWER_PAGE_PROGRESS);
    }

    @Override
    public void showWord(@NonNull VocabularWord word) {
        logger.debug("showWord() called with: word = [{}]", word);
        delegate.showWord(word);
    }

    @Override
    public void onBackPressed() {
        if (delegate.isDrawerOpen()) {
            delegate.closeDrawer();
        } else {
            getPresenter().onBackPressed();
        }
    }

    @OnClick(R.id.button_flashcards)
    public void onFlashcardsClick() {
        Intent intent = new Intent(this, FlashcardsActivity.class);
        startActivity(intent);
    }


}
