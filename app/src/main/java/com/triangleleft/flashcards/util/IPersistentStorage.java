package com.triangleleft.flashcards.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface IPersistentStorage {

    <T> void put(@NonNull String key, @NonNull T value);

    @Nullable
    <T> T get(@NonNull String key, @NonNull Class<T> clazz);

    @Nullable
    <T> T get(@NonNull String key, @NonNull Class<T> clazz, @Nullable T defaultValue);
}
