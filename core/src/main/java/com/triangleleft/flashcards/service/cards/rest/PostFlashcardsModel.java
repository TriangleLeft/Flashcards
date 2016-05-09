package com.triangleleft.flashcards.service.cards.rest;

import com.google.gson.annotations.SerializedName;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.triangleleft.flashcards.service.cards.FlashcardsResult;

import java.util.List;

public class PostFlashcardsModel {

    @SerializedName("flashcard_results")
    List<FlashcardResultModel> flashcardResults;
    @SerializedName("learning_language")
    String learningLanguage;
    @SerializedName("ui_language")
    String uiLanguage;

    public PostFlashcardsModel(String learningLanguage, String uiLanguage, List<FlashcardsResult> results) {
        this.learningLanguage = learningLanguage;
        this.uiLanguage = uiLanguage;
        flashcardResults = Stream.of(results).map(FlashcardResultModel::new).collect(Collectors.toList());
    }

    private static class FlashcardResultModel {

        @SerializedName("id")
        String id;
        @SerializedName("count")
        int count = 1;
        @SerializedName("correct")
        int correct;

        private FlashcardResultModel(FlashcardsResult result) {
            id = result.getId();
            correct = result.isCorrect() ? 1 : 0;
        }

    }
}
