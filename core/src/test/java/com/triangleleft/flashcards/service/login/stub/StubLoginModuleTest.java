/*
 *
 * =======================================================================
 *
 * Copyright (c) 2014-2015 Domlex Limited. All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Domlex Limited.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Domlex Limited.
 *
 * =======================================================================
 *
 */

package com.triangleleft.flashcards.service.login.stub;

import com.triangleleft.flashcards.service.login.exception.LoginException;
import com.triangleleft.flashcards.service.login.exception.PasswordException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.runners.MockitoJUnitRunner;
import rx.observers.TestSubscriber;

import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class StubLoginModuleTest {

    private StubLoginModule module;
    private TestSubscriber<Void> subscriber = TestSubscriber.create();

    @Before
    public void before() {
        module = new StubLoginModule();
    }

    @Test
    public void loginError() throws Exception {
        module.login("fff", "pass").subscribe(subscriber);
        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);

        subscriber.assertError(LoginException.class);
    }

    @Test
    public void passwordError() throws Exception {
        module.login("login", "fff").subscribe(subscriber);
        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);

        subscriber.assertError(PasswordException.class);
    }

    @Test
    public void validLogin() throws Exception {
        module.login("login", "pass").subscribe(subscriber);
        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);

        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
    }
}