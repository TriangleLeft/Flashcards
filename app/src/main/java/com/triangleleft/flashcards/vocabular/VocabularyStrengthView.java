package com.triangleleft.flashcards.vocabular;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.triangleleft.flashcards.R;

public class VocabularyStrengthView extends ImageView {

    public VocabularyStrengthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VocabularyStrengthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VocabularyStrengthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setStrength(@IntRange(from = 0, to = 4) int strength) {
        switch (strength) {
            default:
            case 0:
                setTintedDrawable(R.drawable.ic_signal_cellular_0_bar_black_24dp, R.color.red);
                break;
            case 1:
                setTintedDrawable(R.drawable.ic_signal_cellular_1_bar_black_24dp, R.color.red);
                break;
            case 2:
                setTintedDrawable(R.drawable.ic_signal_cellular_2_bar_black_24dp, R.color.orange);
                break;
            case 3:
                setTintedDrawable(R.drawable.ic_signal_cellular_3_bar_black_24dp, R.color.lime);
                break;
            case 4:
                setTintedDrawable(R.drawable.ic_signal_cellular_4_bar_black_24dp, R.color.green);
                break;
        }
    }

    private void setTintedDrawable(@DrawableRes int drawableId, @ColorRes int colorId) {
        Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), drawableId));
        DrawableCompat.setTint(drawable, ContextCompat.getColor(getContext(), colorId));
        setImageDrawable(drawable);
    }
}
