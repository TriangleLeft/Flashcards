package com.triangleleft.flashcards.page;

import org.openqa.selenium.WebElement;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;

public class VocabularyWordPage extends BasePage {

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocabulary_word_title")
    @iOSFindBy(accessibility = "vocabulary_word_title")
    public WebElement title;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocabulary_word_translation_value")
    @iOSFindBy(accessibility = "vocabulary_word_translation_value")
    public WebElement translation;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocabulary_word_gender_value")
    @iOSFindBy(accessibility = "vocabulary_word_gender_value")
    public WebElement gender;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocabulary_word_pos_value")
    @iOSFindBy(accessibility = "vocabulary_word_pos_value")
    public WebElement pos;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocabular_word_voice")
    @iOSFindBy(accessibility = "vocabular_word_voice")
    public WebElement voiceOver;

    @iOSFindBy(xpath = "//UIAApplication[1]/UIAWindow[1]/UIANavigationBar[1]/UIAButton[1]")
    public WebElement backButton;

    public VocabularyWordPage(AppiumFieldDecorator driver) {
        super(driver);
    }
}
