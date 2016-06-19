package com.triangleleft.flashcards;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.PersistentStorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

@FunctionsAreNonnullByDefault
public class SharedPreferencesPersistentStorage implements PersistentStorage {

    private final static String NAME = "flashcards";
    private final SharedPreferences sharedPreferences;
    private final Gson gson = new GsonBuilder().create();

    public SharedPreferencesPersistentStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME,
                Context.MODE_PRIVATE);
    }

    @Override
    public <T> void put(String key, @Nullable T value) {
        String json = gson.toJson(value);
        sharedPreferences.edit().putString(key, json).apply();
    }

    @Nullable
    @Override
    public <T> T get(String key, Class<T> clazz) {
        String json = sharedPreferences.getString(key, null);
        return gson.fromJson(json, clazz);
    }

    @Nullable
    @Override
    public <T> T get(String key, Class<T> clazz, @Nullable T defaultValue) {
        T value = get(key, clazz);
        if (value != null) {
            return value;
        } else {
            return defaultValue;
        }
    }
}
