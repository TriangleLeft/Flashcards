package com.triangleleft.flashcards.service.cards.rest;

import com.google.gson.annotations.SerializedName;
import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardWord;

import java.util.Collections;
import java.util.List;

public class FlashcardResponseModel implements FlashcardTestData {

    @SerializedName("ui_language")
    public String uiLanguage;
    @SerializedName("learning_language")
    public String learningLanguage;
    @SerializedName("flashcard_data")
    public List<FlashcardModel> flashcardData;

    @Override
    public String getUiLanguage() {
        return uiLanguage;
    }

    @Override
    public String getLearningLanguage() {
        return learningLanguage;
    }

    public List<FlashcardWord> getWords() {
        return Collections.unmodifiableList(flashcardData);
    }

    public static class FlashcardModel implements FlashcardWord {

        @SerializedName("ui_word")
        public String translation;
        @SerializedName("id")
        public String id;
        @SerializedName("learning_word")
        public String word;

        @Override
        public String getWord() {
            return word;
        }

        @Override
        public String getTranslation() {
            return translation;
        }

        @Override
        public String getId() {
            return id;
        }
    }

}
