package com.triangleleft.flashcards.service.cards.rest;

import static com.annimon.stream.Collectors.toList;

import com.annimon.stream.Stream;
import com.google.gson.annotations.SerializedName;
import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardWord;

import java.util.List;

public class FlashcardResponseModel {

    @SerializedName("ui_language")
    String uiLanguage;
    @SerializedName("learning_language")
    String learningLanguage;
    @SerializedName("flashcard_data")
    List<FlashcardModel> flashcardData;

    public FlashcardTestData toTestData() {
        List<FlashcardWord> words = Stream.of(flashcardData)
            .map(FlashcardModel::toWord)
            .collect(toList());
        return FlashcardTestData.create(uiLanguage, learningLanguage, words);
    }

    public static class FlashcardModel {

        @SerializedName("ui_word")
        String translation;
        @SerializedName("id")
        String id;
        @SerializedName("learning_word")
        String word;

        public FlashcardWord toWord() {
            return FlashcardWord.create(word, translation, id);
        }
    }

}
