package com.triangleleft.flashcards.page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;

public class VocabularyWordPage extends BasePage {

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocabulary_word_title")
    public WebElement title;
    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocabulary_word_translation_value")
    public WebElement translation;
    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocabulary_word_gender_value")
    public WebElement gender;
    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocabulary_word_pos_value")
    public WebElement pos;
    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocabular_word_voice")
    public WebElement voiceOver;

    public VocabularyWordPage(AppiumFieldDecorator driver) {
        super(driver);
    }
}
