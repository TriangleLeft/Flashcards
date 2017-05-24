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

package com.triangleleft.flashcards.ui.vocabular;

import com.triangleleft.flashcards.ui.vocabular.word.VocabularyWordPresenter;
import com.triangleleft.flashcards.ui.vocabular.word.VocabularyWordView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class VocabularyWordPresenterTest {

    @Mock
    VocabularyWordView view;
    private VocabularyWordPresenter presenter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        presenter = new VocabularyWordPresenter(null);
    }

    @Test
    public void showWord() throws Exception {
//        VocabularyWord word = mock(VocabularyWord.class);
//        presenter.onCreate();
//        presenter.onBind(view);
//
//        presenter.showWord(Optional.of(word));
//
//        verify(view).showWord(word);
    }

//    @Test
//    public void showEmptyWord() throws Exception {
//        presenter.onCreate();
//        presenter.onBind(view);
//
//        presenter.showWord(Optional.empty());
//
//        verify(view).showEmpty();
//    }
}