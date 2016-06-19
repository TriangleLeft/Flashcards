package com.triangleleft.flashcards.util;

import android.support.annotation.Nullable;

@FunctionsAreNonnullByDefault
public interface PersistentStorage {

    <T> void put(String key, @Nullable T value);

    @Nullable
    <T> T get(String key, Class<T> clazz);

    @Nullable
    <T> T get(String key, Class<T> clazz, @Nullable T defaultValue);
}
