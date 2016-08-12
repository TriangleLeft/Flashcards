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

package com.triangleleft.flashcards.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class ListUtilsTest {

    @Test
    public void wrapNullList() {
        assertThat(ListUtils.wrapList(null), is(Collections.emptyList()));
    }

    @Test
    public void wrapList() {
        List<String> list = new ArrayList<>();
        assertThat(ListUtils.wrapList(list), is(sameInstance(list)));
    }
}