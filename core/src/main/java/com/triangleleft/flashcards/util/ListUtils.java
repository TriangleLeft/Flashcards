package com.triangleleft.flashcards.util;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

public class ListUtils {

    public static <T> List<T> wrapList(@Nullable List<T> list) {
        if (list == null) {
            return Collections.emptyList();
        } else {
            return list;
        }
    }
}
