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
import io.appium.java_client.AppiumDriver;

public class FlashcardsApp {

    private final AppiumDriver driver;

    public FlashcardsApp(AppiumDriver driver) {
        this.driver = driver;
    }

    public LoginPage loginPage() {
        return new LoginPage(driver);
    }

    public MainPage mainPage() {
        return new MainPage(driver);
    }

    public VocabularyListPage vocabularyListPage() {
        return new VocabularyListPage(driver);
    }

    public DrawerPage drawerPage() {
        return new DrawerPage(driver);
    }

    public FlashcardPage flashcardPage() {
        return new FlashcardPage(driver);
    }
}
