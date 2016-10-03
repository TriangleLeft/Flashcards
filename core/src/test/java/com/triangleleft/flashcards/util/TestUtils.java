package com.triangleleft.flashcards.util;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.concurrent.Executor;

public class TestUtils {

    public static Executor immediateExecutor() {
        Executor mock = Mockito.mock(Executor.class);
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        Mockito.doAnswer(invocation -> {
            runnableCaptor.getValue().run();
            return null;
        }).when(mock).execute(runnableCaptor.capture());
        return mock;
    }


}
