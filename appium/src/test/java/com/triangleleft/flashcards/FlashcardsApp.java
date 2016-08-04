package com.triangleleft.flashcards;/*
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

import com.triangleleft.flashcards.page.DrawerPage;
import com.triangleleft.flashcards.page.FlashcardPage;
import com.triangleleft.flashcards.page.LoginPage;
import com.triangleleft.flashcards.page.MainPage;
import com.triangleleft.flashcards.page.VocabularyListPage;
import com.triangleleft.flashcards.page.VocabularyWordPage;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class FlashcardsApp {

    private final AppiumFieldDecorator decorator;

    public FlashcardsApp(AppiumFieldDecorator decorator) {
        this.decorator = decorator;
    }

    public LoginPage loginPage() {
        return new LoginPage(decorator);
    }

    public MainPage mainPage() {
        return new MainPage(decorator);
    }

    public VocabularyListPage vocabularyListPage() {
        return new VocabularyListPage(decorator);
    }

    public VocabularyWordPage vocabularyWordPage() {
        return new VocabularyWordPage(decorator);
    }

    public DrawerPage drawerPage() {
        return new DrawerPage(decorator);
    }

    public FlashcardPage flashcardPage() {
        return new FlashcardPage(decorator);
    }
}
