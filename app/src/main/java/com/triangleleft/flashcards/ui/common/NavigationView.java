package com.triangleleft.flashcards.ui.common;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Picasso;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.di.main.MainPageComponent;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.ui.main.DrawerLanguagesAdapter;
import com.triangleleft.flashcards.ui.main.DrawerPresenter;
import com.triangleleft.flashcards.ui.main.IDrawerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.animation.FloatEvaluator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;

public class NavigationView extends FrameLayout implements IDrawerView {

    private static final Logger logger = LoggerFactory.getLogger(NavigationView.class);

    @Bind(R.id.drawer_list)
    RecyclerView recyclerView;
    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @Bind(R.id.drawer_user_name)
    TextView drawerUserName;
    @Bind(R.id.drawer_user_avatar)
    ImageView drawerUserAvatar;
    @Bind(R.id.drawer_divider)
    View divider;
    @Bind(R.id.drawer_overlay)
    View overlay;
    @Bind(R.id.drawer_content)
    View content;
    @BindDimen(R.dimen.avatar_size)
    float avatarSize;
    @BindDimen(R.dimen.drawer_item_language_icon_size)
    float drawerIconSize;
    @BindDimen(R.dimen.drawer_list_slide_offset)
    float drawerListSlideOffset;
    @BindDimen(R.dimen.drawer_header_avatar_margin_top)
    float drawerHeaderAvatarMarginTop;
    @BindDimen(R.dimen.activity_vertical_margin)
    float activityVerticalMargin;

    @Inject
    FlagImagesProvider flagImagesProvider;
    @Inject
    DrawerPresenter presenter;

    private final FloatEvaluator evaluator = new FloatEvaluator();
    private DrawerLanguagesAdapter adapter;
    private final LinearLayoutManager layoutManager;

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_navigation, this, true);
        ButterKnife.bind(this);

        drawerUserAvatar.setPivotX(-1 * drawerUserAvatar.getMeasuredWidth() / 2);
        drawerUserAvatar.setPivotY(-1 * drawerUserAvatar.getMeasuredHeight() / 2);

        layoutManager = new LinearLayoutManager(context) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                if (!isSmoothScrolling()) {
                    LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

                        private static final float SPEED = 150f;

                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return SPEED / displayMetrics.densityDpi;
                        }
                    };
                    linearSmoothScroller.setTargetPosition(position);
                    startSmoothScroll(linearSmoothScroller);
                }
            }
        };
        recyclerView.setLayoutManager(layoutManager);
    }

    public void init(MainPageComponent component) {
        component.inject(this);
        adapter = new DrawerLanguagesAdapter(flagImagesProvider,
                (viewHolder, position) -> presenter.onLanguageSelected(adapter.getItem(position)));
        recyclerView.setAdapter(adapter);
        presenter.onCreate();
        presenter.onBind(this);
    }

    public void setAnimationProgress(float value) {
        // 1f - expanded, 0f - collapsed
        Float avatarScale = evaluator.evaluate(value, drawerIconSize / avatarSize, 1);
        drawerUserAvatar.setScaleX(avatarScale);
        drawerUserAvatar.setScaleY(avatarScale);

        Float contentOffset = evaluator.evaluate(value, activityVerticalMargin * 2 - drawerHeaderAvatarMarginTop, 0);
        content.setTranslationY(contentOffset);

        Float alpha = evaluator.evaluate(value, -1f, 1f);
        drawerUserName.setAlpha(alpha);
        recyclerView.setAlpha(alpha);
        divider.setAlpha(alpha);

        Float listOffset = evaluator.evaluate(value, -drawerListSlideOffset, 0);
        recyclerView.setTranslationY(listOffset);
        drawerUserName.setTranslationY(listOffset);
        divider.setTranslationY(listOffset);

        if (value == 1f) {
            overlay.setVisibility(View.GONE);
        } else {
            overlay.setVisibility(View.VISIBLE);
        }
    }

    public void setOnOverlayClickListener(OnClickListener listener) {
        overlay.setOnClickListener(listener);
    }

    @Override
    public void showUserData(String username, String avatar, List<Language> languages) {
        logger.debug("showUserData() called with: username = [{}], avatar = [{}], languages = [{}]", username, avatar,
                languages);
        Picasso.with(getContext()).load(avatar).into(drawerUserAvatar);
        drawerUserName.setText(username);
        recyclerView.setVisibility(View.VISIBLE);
        progressWheel.setVisibility(View.INVISIBLE);

//        // Need to post via handler, otherwise there are visual issues (probably because at this frame list is not
//        // visible yet)
//        post(() -> {
        recyclerView.scrollToPosition(0);
        adapter.setData(languages);
//        });
    }

    @Override
    public void notifyLanguageSwitchError() {
        Toast.makeText(getContext(), R.string.language_switch_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDrawerProgress() {
        recyclerView.setVisibility(View.INVISIBLE);
        progressWheel.setVisibility(View.VISIBLE);
    }

    public void resetScroll() {
        recyclerView.smoothScrollToPosition(0);
    }
}
