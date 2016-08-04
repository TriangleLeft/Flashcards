package com.triangleleft.flashcards.ui.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.triangleleft.flashcards.R;

import java.util.HashMap;
import java.util.Map;

public class FlagImagesProvider {

    private final Context context;
    private final Map<String, Integer> flagMap = new HashMap<>();

    {
        flagMap.put("de", R.drawable.germany);
        flagMap.put("fr", R.drawable.france);
        flagMap.put("es", R.drawable.spain);
        flagMap.put("dn", R.drawable.netherlands);
        flagMap.put("ru", R.drawable.russia);
        flagMap.put("uk", R.drawable.ukraine);
        flagMap.put("pt", R.drawable.brazil);
        flagMap.put("vi", R.drawable.viewtnam);
        flagMap.put("nb", R.drawable.norway);
        flagMap.put("tr", R.drawable.turkey);
        flagMap.put("sv", R.drawable.sweden);
        flagMap.put("da", R.drawable.denmark);
        flagMap.put("cy", R.drawable.wales);
        flagMap.put("ga", R.drawable.ireland);
        flagMap.put("it", R.drawable.italy);
        flagMap.put("pl", R.drawable.polish);
        flagMap.put("he", R.drawable.israel);
    }

    public FlagImagesProvider(Context context) {
        this.context = context;
    }

    public Drawable getFlag(String languageId) {
        Integer resource = flagMap.get(languageId);
        if (resource != null) {
            return ContextCompat.getDrawable(context, resource);
        } else {
            return ContextCompat.getDrawable(context, R.drawable.unknown);
        }
    }
}
