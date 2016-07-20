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

package com.triangleleft.flashcards.service.settings.rest;

import static org.mockito.Mockito.*;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.settings.rest.model.UserDataModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class RestSettingsModuleTest {

    private RestSettingsModule module;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    RestService service;
    @Mock
    AccountModule accountModule;

    @Before
    public void before() {
        module = new RestSettingsModule(service, accountModule);
    }

    @Test
    public void loadUserData() {
        when(accountModule.getUserId()).thenReturn(Optional.of("id"));
        UserDataModel model = mock(UserDataModel.class);
        UserData userData = mock(UserData.class);
        when(model.toUserData()).thenReturn(userData);
        when(service.getUserData("id")).thenReturn(Observable.just(model));

        TestSubscriber<UserData> subscriber = TestSubscriber.create();
        module.loadUserData().subscribe(subscriber);
        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);

        subscriber.assertValue(userData);
        verify(accountModule).setUserData(userData);
    }

    @Test
    public void switchLanguage() {
        Language language = mock(Language.class);
        when(language.getId()).thenReturn("id");
        when(service.switchLanguage("id")).thenReturn(Observable.just(null));

        TestSubscriber<Void> subscriber = TestSubscriber.create();
        module.switchLanguage(language).subscribe(subscriber);
        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);

        subscriber.assertNoErrors();
        subscriber.assertValue(null);
    }

    @Test
    public void failWithUserId() {
        when(accountModule.getUserId()).thenReturn(Optional.empty());
        expectedException.expect(IllegalStateException.class);

        module.loadUserData();
    }
}