package com.triangleleft.flashcards.util;

import com.triangleleft.flashcards.Observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class TestObserver<T> implements Observer<T> {

    private final List<T> data = Collections.synchronizedList(new ArrayList<>());
    private final List<Throwable> throwables = Collections.synchronizedList(new ArrayList<>());

    public TestObserver() {

    }

    @Override
    public void onError(Throwable throwable) {
        throwables.add(throwable);
    }

    @Override
    public void onNext(T data) {
        this.data.add(data);
    }

    public void assertOnNextCalled() {
        assertThat("onNext was not called", data.size(), greaterThan(0));
    }

    public void assertOnErrorCalled() {
        assertThat("onError was not called", throwables.size(), greaterThan(0));
    }

    public void assertValue(T data) {
        assertOnNextCalled();
        assertThat(this.data.get(0), equalTo(data));
    }

    public void assertError(Throwable throwable) {
        assertOnErrorCalled();
        assertThat(throwables.get(0), equalTo(throwable));
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
