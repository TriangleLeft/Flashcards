package com.triangleleft.flashcards.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomProgressBar extends ProgressBar {

    public static boolean disabled;

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (disabled) {
            stopAnimation();
        }
    }

    @Override
    public void setVisibility(int v) {
        super.setVisibility(v);
        if (disabled) {
            stopAnimation();
        }
    }

    private void stopAnimation() {
        try {
            Method method = ProgressBar.class.getDeclaredMethod("stopAnimation");
            method.setAccessible(true);
            method.invoke(this);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
