package com.triangleleft.flashcards.util;

import com.triangleleft.flashcards.Observer;

import org.junit.Assert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.fail;

public class TestObserver<T> implements Observer<T> {

    private final T data;
    private final Throwable throwable;
    boolean onErrorCalled;
    boolean onNextCalled;

    public static <T> TestObserver<T> expecting(T data) {
        return new TestObserver<>(data, null);
    }

    public static <T> TestObserver<T> expecting(Throwable throwable) {
        return new TestObserver<>(null, throwable);
    }

    private TestObserver(T data, Throwable throwable) {
        this.data = data;
        this.throwable = throwable;
    }

    @Override
    public void onError(Throwable throwable) {
        onErrorCalled = true;
        if (this.throwable != null) {
            assertThat(throwable, equalTo(this.throwable));
        } else {
            fail("Got unexpected onError call with " + throwable);
        }
    }

    @Override
    public void onNext(T data) {
        onNextCalled = true;
        if (this.data != null) {
            Assert.assertThat(data, equalTo(this.data));
        } else {
            fail("Got unexpected onNext call with " + data);
        }
    }

    public void assertOnNextCalled() {
        Assert.assertThat(onNextCalled, equalTo(true));
    }

    public void assertOnErrorCalled() {
        Assert.assertThat(onErrorCalled, equalTo(true));
    }

}
