package com.triangleleft.flashcards.util;

import com.triangleleft.flashcards.Action;

import org.junit.Ignore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@Ignore
@FunctionsAreNonnullByDefault
public class TestAction<T> implements Action<T> {

    private final List<T> data = Collections.synchronizedList(new ArrayList<>());

    public TestAction() {

    }

    @Override
    public void call(T data) {
        this.data.add(data);
    }

    public void assertOnNextCalled() {
        assertThat("onNext was not called", data.size(), greaterThan(0));
    }

    public void assertValue(T data) {
        assertOnNextCalled();
        assertThat(this.data.get(0), equalTo(data));
    }

    public List<T> getOnNextEvents() {
        return data;
    }

    public void awaitValueCount(int valueCount, long duration, TimeUnit unit) {
        // quick and dirty
        long currentTime = System.currentTimeMillis();
        long endTime = currentTime + unit.toMillis(duration);
        while (data.size() < valueCount && currentTime <= endTime) {
            // placebo
            Thread.yield();
            currentTime = System.currentTimeMillis();
        }
        if (data.size() < valueCount) {
            fail("Await timed out");
        }
    }
}
