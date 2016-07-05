package com.triangleleft.flashcards.common;

import com.triangleleft.flashcards.R;

import android.animation.FloatEvaluator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavigationView extends FrameLayout {

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
    private final FloatEvaluator evaluator;

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_navigation, this, true);
        ButterKnife.bind(this);

        evaluator = new FloatEvaluator();
        drawerUserAvatar.setPivotX(-1 * drawerUserAvatar.getMeasuredWidth() / 2);
        drawerUserAvatar.setPivotY(-1 * drawerUserAvatar.getMeasuredHeight() / 2);
    }

    public void setAnimationProgress(float value) {
        Float avatarScale = evaluator.evaluate(value, 40f / 64f, 1);
        drawerUserAvatar.setScaleX(avatarScale);
        drawerUserAvatar.setScaleY(avatarScale);

        Float alpha = evaluator.evaluate(value, -1f, 1f);
        drawerUserName.setAlpha(alpha);
        divider.setAlpha(alpha);

        Float listOffset = evaluator.evaluate(value, -50, 0);
        drawerContentFlipper.setTranslationY(listOffset);
        drawerUserName.setTranslationY(listOffset);
        divider.setTranslationY(listOffset);
    }

}
