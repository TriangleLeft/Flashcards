package com.triangleleft.flashcards.util;

import android.support.annotation.Nullable;

public class TextUtils {

    private TextUtils() {
        // Static use only
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable String str) {
        return str == null || str.length() == 0;
    }

    public static boolean notEquals(@Nullable String a, @Nullable String b) {
        return !TextUtils.equals(a, b);
    }

    /**
     * Check whether provided string has text, that is any non-space symbols.
     *
     * @param string string to check
     * @return true if string has text, false otherwise
     */
    public static boolean hasText(@Nullable String string) {
        return !isEmpty(string) && !string.trim().isEmpty();
    }

    /**
     * Returns true if a and b are equal, including if they are both null.
     * <p><i>Note: In platform versions 1.1 and earlier, this method only worked well if
     * both the arguments were instances of String.</i></p>
     *
     * @param a first CharSequence to check
     * @param b second CharSequence to check
     * @return true if a and b are equal
     */
    public static boolean equals(@Nullable String a, @Nullable String b) {
        return a != null && a.equals(b);
    }
}
