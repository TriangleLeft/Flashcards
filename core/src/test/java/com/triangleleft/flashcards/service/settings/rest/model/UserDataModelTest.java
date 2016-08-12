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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.UserData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

@RunWith(JUnit4.class)
public class UserDataModelTest {

    private UserDataModel model;

    @Before
    public void before() {
        model = new UserDataModel();
    }

    @Test
    public void toUserData() {
        model.username = "name";
        model.email = "email";
        model.learningLanguage = "learn";
        model.uiLanguage = "ui";
        model.avatar = "//url";
        LanguageDataModel langModel = mock(LanguageDataModel.class);
        Language language = Language.create("", "", 1, false, false);
        when(langModel.toLanguage()).thenReturn(language);
        model.languages = Collections.singletonList(langModel);

        UserData userData = model.toUserData();

        assertThat(userData.getUiLanguageId(), equalTo("ui"));
        assertThat(userData.getLearningLanguageId(), equalTo("learn"));
        assertThat(userData.getUsername(), equalTo("name"));
        assertThat(userData.getAvatar(), equalTo(String.format(UserDataModel.URL_FORMAT, "//url")));
        assertThat(userData.getLanguages(), hasItem(language));
    }

}