package com.triangleleft.flashcards.service.common.error;

import com.google.gson.stream.MalformedJsonException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class CommonErrorTest {

    @Test
    public void buildInternalErrorFromEmptyThrowable() {
        CommonError error = CommonError.fromThrowable(null);
        assertEquals(ErrorType.INTERNAL, error.getType());
    }

    @Test
    public void buildConversionErrorFromJsonThrowable() {
        CommonError error = CommonError.fromThrowable(new MalformedJsonException("fail"));
        assertEquals(ErrorType.CONVERSION, error.getType());
    }

    @Test
    public void buildNetworkErrorFromIOException() {
        CommonError error = CommonError.fromThrowable(new IOException("fail"));
        assertEquals(ErrorType.NETWORK, error.getType());
    }

    @Test
    public void buildInternalErrorFromThrowable() {
        CommonError error = CommonError.fromThrowable(new Throwable("fail"));
        assertEquals(ErrorType.INTERNAL, error.getType());
    }


}
