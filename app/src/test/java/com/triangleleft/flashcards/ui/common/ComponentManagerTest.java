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

package com.triangleleft.flashcards.ui.common;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.di.IComponent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for {@link ComponentManager}.
 */
@RunWith(JUnit4.class)
public class ComponentManagerTest {

    private ComponentManager manager;

    @Before
    public void before() {
        manager = ComponentManager.buildDefault();
    }

    @Test
    public void restoreEmptyComponent() throws Exception {
        Optional<IComponent> optional = manager.restoreComponent(getClass());

        assertThat(optional, is(Optional.empty()));
    }

    @Test
    public void saveComponent() throws Exception {
        IComponent component = Mockito.mock(IComponent.class);

        manager.saveComponent(getClass(), component);

        Optional<IComponent> optional = manager.restoreComponent(getClass());
        assertThat(optional.get(), is(sameInstance(component)));
    }
}