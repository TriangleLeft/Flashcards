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

package com.triangleleft.flashcards.service.account;

import com.triangleleft.flashcards.util.PersistentStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class SimpleAccountModuleTest {

    private SimpleAccountModule module;
    private static final String KEY_USER_ID = "SimpleAccountModule::userId";
    private static final String KEY_LOGIN = "SimpleAccountModule::login";
    private static final String KEY_REMEMBER_USER = "SimpleAccountModule::rememberUser";
    private static final String KEY_USERDATA = "SimpleAccountModule::userData";

    @Mock
    PersistentStorage storage;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        module = new SimpleAccountModule(storage);
    }

    @Test
    public void userId() throws Exception {
        // Check that first we would try to get value from storage
        when(storage.get(KEY_USER_ID, String.class)).thenReturn("store");
        assertThat(module.getUserId().get(), equalTo("store"));

        // Check that afterward we would return in-memory value
        reset(storage);
        module.getUserId();
        verify(storage, never()).get(KEY_USER_ID, String.class);

        // Check that set would persist value
        module.setUserId("value");
        verify(storage).put(KEY_USER_ID, "value");
    }

//    @Test
//    public void login() throws Exception {
//        when(storage.get(KEY_LOGIN, String.class)).thenReturn("store");
//        assertThat(module.getLogin().get(), equalTo("store"));
//
//        reset(storage);
//        module.getLogin();
//        verify(storage, never()).get(KEY_LOGIN, String.class);
//
//        module.setLogin("value");
//        verify(storage).put(KEY_LOGIN, "value");
//    }
//
//    @Test
//    public void userData() throws Exception {
//        UserData store = mock(UserData.class);
//        when(storage.get(KEY_USERDATA, UserData.class)).thenReturn(store);
//        assertThat(module.getUserData().get(), equalTo(store));
//
//        reset(storage);
//        module.getUserData();
//        verify(storage, never()).get(KEY_USERDATA, UserData.class);
//
//        UserData value = mock(UserData.class);
//        module.setUserData(value);
//        verify(storage).put(KEY_USERDATA, value);
//    }
//
//    @Test
//    public void rememberUser() {
//        // Default value is false
//        assertThat(module.shouldRememberUser(), equalTo(false));
//
//        when(storage.get(KEY_REMEMBER_USER, Boolean.class)).thenReturn(true);
//        assertThat(module.shouldRememberUser(), equalTo(true));
//
//        reset(storage);
//        module.shouldRememberUser();
//        verify(storage, never()).get(KEY_REMEMBER_USER, Boolean.class);
//
//        module.setRememberUser(false);
//        verify(storage).put(KEY_REMEMBER_USER, false);
//    }
}