package com.triangleleft.flashcards.page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;

import java.util.List;

public class VocabularyListPage extends BasePage {

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/vocab_list")
    public WebElement list;
    @AndroidFindBy(id = "com.triangleleft.flashcards:id/item_vocabular_text")
    public List<WebElement> words;

    public VocabularyListPage(AppiumFieldDecorator driver) {
        super(driver);
    }
}
