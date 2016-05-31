package com.triangleleft.flashcards.common;

import com.triangleleft.flashcards.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

public class FlagImagesProvider {

    private final Context context;
    private final Map<String, Integer> flagMap = new HashMap<>();

    {
        flagMap.put("de", R.drawable.germany);
        flagMap.put("fr", R.drawable.france);
        flagMap.put("es", R.drawable.spain);
    }

    public FlagImagesProvider(Context context) {
        this.context = context;
    }

    public Drawable getFlag(String languageId) {
        Integer resource = flagMap.get(languageId);
        if (resource != null) {
            return ContextCompat.getDrawable(context, resource);
        } else {
            return new ColorDrawable(Color.RED);
        }
    }
}
