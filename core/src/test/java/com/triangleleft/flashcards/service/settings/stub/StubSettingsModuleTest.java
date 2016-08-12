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

package com.triangleleft.flashcards.service.settings.stub;

import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.UserData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.runners.MockitoJUnitRunner;
import rx.observers.TestSubscriber;

import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class StubSettingsModuleTest {

    private StubSettingsModule module;

    @Before
    public void before() {
        module = new StubSettingsModule();
    }

    @Test
    public void loadUserData() throws Exception {
        TestSubscriber<UserData> subscriber = TestSubscriber.create();
        module.loadUserData().subscribe(subscriber);
        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);

        subscriber.assertValueCount(1);
    }

    @Test
    public void switchLanguage() throws Exception {
        Language language = Language.create("es", "Spanish", 3, true, false);
        TestSubscriber<Void> subscriber = TestSubscriber.create();
        module.switchLanguage(language).subscribe(subscriber);
        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);

        subscriber.assertValueCount(1);
    }
}