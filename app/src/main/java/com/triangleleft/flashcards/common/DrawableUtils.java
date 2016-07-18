/*
 *
 * =======================================================================
 *
 * Copyright (c) 2014-2015 Domlex Limited. All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Domlex Limited.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Domlex Limited.
 *
 * =======================================================================
 *
 */

package com.triangleleft.flashcards.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
public class DrawableUtils {

    public static Drawable getTintedDrawable(Context context, @DrawableRes int drawableResId,
        @ColorRes int colorResId) {
        Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, drawableResId));
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, colorResId));
        return drawable;
    }
}
