package com.triangleleft.flashcards.page;

import com.annimon.stream.Stream;

import org.openqa.selenium.WebElement;

import java.util.List;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;

public class VocabularyListPage extends BasePage {

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocab_list")
    @iOSFindBy(xpath = "//UIAApplication[1]/UIAWindow[1]/UIATableView[2]")
    public WebElement list;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/item_vocabular_text")
    @iOSFindBy(accessibility = "item_vocabular_text")
    public List<WebElement> words;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocabulary_list_button_retry")
    // TODO: missing
    public WebElement retryButton;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocabulary_list_no_words")
    // TODO: missing
    public WebElement noWordsLabel;

    public VocabularyListPage(AppiumFieldDecorator driver) {
        super(driver);
    }

    public void clickOn(String word) {
        // Get our word
        WebElement webElement = Stream.of(words)
                .filter(element -> word.equals(element.getText()))
                .findFirst().get();
        // Click it
        webElement.click();
    }
}
