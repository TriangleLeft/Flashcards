package com.triangleleft.flashcards.common;

import android.animation.FloatEvaluator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.main.DrawerLanguagesAdapter;

public class NavigationView extends FrameLayout {

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

    private final FloatEvaluator evaluator;

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_navigation, this, true);
        ButterKnife.bind(this);

        evaluator = new FloatEvaluator();
        drawerUserAvatar.setPivotX(-1 * drawerUserAvatar.getMeasuredWidth() / 2);
        drawerUserAvatar.setPivotY(-1 * drawerUserAvatar.getMeasuredHeight() / 2);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void setAnimationProgress(float value) {
        Float avatarScale = evaluator.evaluate(value, drawerIconSize / avatarSize, 1);
        drawerUserAvatar.setScaleX(avatarScale);
        drawerUserAvatar.setScaleY(avatarScale);

        Float contentOffset = evaluator.evaluate(value, activityVerticalMargin * 2 - drawerHeaderAvatarMarginTop, 0);
        content.setTranslationY(contentOffset);

        Float alpha = evaluator.evaluate(value, -1f, 1f);
        drawerUserName.setAlpha(alpha);
        drawerContentFlipper.setAlpha(alpha);
        divider.setAlpha(alpha);

        Float listOffset = evaluator.evaluate(value, -drawerListSlideOffset, 0);
        drawerContentFlipper.setTranslationY(listOffset);
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

    public void setLanguagesAdapter(DrawerLanguagesAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void showUserData(String username, String avatar) {
        Picasso.with(getContext()).load(avatar).into(drawerUserAvatar);
        drawerUserName.setText(username);
        drawerContentFlipper.setDisplayedChild(DRAWER_PAGE_CONTENT);
    }

    public void showDrawerProgress() {
        drawerContentFlipper.setDisplayedChild(DRAWER_PAGE_PROGRESS);
    }
}
