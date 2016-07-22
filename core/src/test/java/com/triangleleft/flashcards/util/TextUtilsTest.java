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
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TextUtilsTest {

    @Test
    public void isEmpty() {
        assertThat(TextUtils.isEmpty(null), is(true));
        assertThat(TextUtils.isEmpty(""), is(true));
        assertThat(TextUtils.isEmpty("w"), is(false));
    }

    @Test
    public void notEquals() {
        assertThat(TextUtils.notEquals(null, null), is(true));
        assertThat(TextUtils.notEquals(null, ""), is(true));
        assertThat(TextUtils.notEquals("", null), is(true));
        assertThat(TextUtils.notEquals("", ""), is(false));
        assertThat(TextUtils.notEquals("qw", "qw"), is(false));
        assertThat(TextUtils.notEquals("qw", "wq"), is(true));
    }

    @Test
    public void hasText() {
        assertThat(TextUtils.hasText(null), is(false));
        assertThat(TextUtils.hasText(""), is(false));
        assertThat(TextUtils.hasText("  "), is(false));
        assertThat(TextUtils.hasText("  s  "), is(true));
    }

    @Test
    public void equals() {
        assertThat(TextUtils.equals(null, null), is(false));
        assertThat(TextUtils.equals(null, ""), is(false));
        assertThat(TextUtils.equals("", null), is(false));
        assertThat(TextUtils.equals("", ""), is(true));
        assertThat(TextUtils.equals("qw", "qw"), is(true));
        assertThat(TextUtils.equals("qw", "wq"), is(false));
    }
}