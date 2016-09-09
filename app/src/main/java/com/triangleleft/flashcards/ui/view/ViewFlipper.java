package com.triangleleft.flashcards.ui.view;

import com.triangleleft.flashcards.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

public class ViewFlipper extends android.widget.ViewFlipper {

    private final int previewChild;

    public ViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewFlipper);
        previewChild = a.getInt(R.styleable.ViewFlipper_previewChild, 0);
        a.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (i == previewChild) {
                    child.setVisibility(View.VISIBLE);
                } else {
                    child.setVisibility(View.GONE);
                }
            }
        }
    }

}
