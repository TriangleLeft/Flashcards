package com.triangleleft.flashcards.util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Ignore;

@Ignore
@FunctionsAreNonnullByDefault
public class TestUtils {
    public static <T> Matcher<Iterable<? extends T>> contains(T data) {
        return new TypeSafeMatcher<Iterable<? extends T>>() {
            @Override
            protected boolean matchesSafely(Iterable<? extends T> items) {
                boolean matched = false;
                for (T item : items) {
                    if (data.equals(item)) {
                        matched = true;
                        break;
                    }
                }
                return matched;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("iterable over ")
                        .appendValue(data)
                        .appendText(" in any order");
            }
        };
    }
}
