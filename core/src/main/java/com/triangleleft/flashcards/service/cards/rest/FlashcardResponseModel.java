package com.triangleleft.flashcards.service.cards.rest;

import static com.annimon.stream.Collectors.toList;

import com.annimon.stream.Stream;
import com.google.gson.annotations.SerializedName;
import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardWord;

import java.util.List;

public class FlashcardResponseModel {

    @SerializedName("ui_language")
    private String uiLanguage;
    @SerializedName("learning_language")
    private String learningLanguage;
    @SerializedName("flashcard_data")
    private List<FlashcardModel> flashcardData;

    public FlashcardTestData toTestData() {
        List<FlashcardWord> words = Stream.of(flashcardData)
            .map(FlashcardModel::toWord)
            .collect(toList());
        return FlashcardTestData.create(uiLanguage, learningLanguage, words);
    }

    public static class FlashcardModel {

        @SerializedName("ui_word")
        private String translation;
        @SerializedName("id")
        private String id;
        @SerializedName("learning_word")
        private String word;

        public FlashcardWord toWord() {
            return FlashcardWord.create(word, translation, id);
        }
    }

}
