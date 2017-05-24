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

package com.triangleleft.flashcards.service.settings.rest.model;

import com.triangleleft.flashcards.service.settings.Language;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(JUnit4.class)
public class LanguageDataModelTest {

    private LanguageDataModel model;

    @Before
    public void before() {
        model = new LanguageDataModel();

    }

    @Test
    public void toLanguage() throws Exception {
        model.currentLearning = true;
        model.languageId = "id";
        model.languageString = "string";
        model.learning = true;
        model.level = 3232;

        Language language = model.toLanguage();

        assertThat(language.getId(), equalTo("id"));
        assertThat(language.getLevel(), equalTo(3232));
        assertThat(language.getName(), equalTo("string"));
        assertThat(language.isCurrentLearning(), equalTo(true));
        assertThat(language.isLearning(), equalTo(true));
    }
}