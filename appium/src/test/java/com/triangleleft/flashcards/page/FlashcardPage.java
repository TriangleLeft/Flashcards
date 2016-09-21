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

package com.triangleleft.flashcards.page;

import org.openqa.selenium.WebElement;

import java.util.List;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;

public class FlashcardPage extends BasePage {

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/flashcard_button_show_answer")
    @iOSFindBy(accessibility = "flashcard_button_show_answer")
    public WebElement showAnswerButton;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/flashcard_word")
    @iOSFindBy(accessibility = "flashcard_word")
    public WebElement word;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/flashcard_translation")
    @iOSFindBy(accessibility = "flashcard_translation")
    public WebElement translation;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/flashcard_button_right")
    @iOSFindBy(accessibility = "flashcard_button_right")
    public WebElement buttonRight;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/flashcard_button_wrong")
    @iOSFindBy(accessibility = "flashcard_button_wrong")
    public WebElement buttonWrong;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/flashcard_button_back")
    public WebElement buttonBack;
    @AndroidFindBy(id = "com.triangleleft.flashcards:id/flashcard_button_restart")
    public WebElement buttonRestart;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/flashcard_result_error_word_value")
    @iOSFindBy(accessibility = "flashcard_result_error_word_value")
    public List<WebElement> wrongWords;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/flashcard_result_error_word_translation")
    @iOSFindBy(accessibility = "flashcard_result_error_word_translation")
    public List<WebElement> wrongWordTranslations;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/flashcard_result_errors_text")
    @iOSFindBy(accessibility = "flashcard_result_errors_text")
    public WebElement resultErrorsText;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/flashcard_result_success_text")
    @iOSFindBy(accessibility = "flashcard_result_success_text")
    public WebElement resultSuccessText;

    public FlashcardPage(AppiumFieldDecorator driver) {
        super(driver);
    }
}
