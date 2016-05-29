package com.triangleleft.flashcards.service.cards.rest;

import com.google.gson.annotations.SerializedName;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.triangleleft.flashcards.service.cards.FlashcardTestResult;
import com.triangleleft.flashcards.service.cards.FlashcardWordResult;

import java.util.List;

public class FlashcardResultsController {

    @SerializedName("flashcard_results")
    List<FlashcardResultModel> flashcardResults;
    @SerializedName("learning_language")
    String learningLanguage;
    @SerializedName("ui_language")
    String uiLanguage;

    public FlashcardResultsController(FlashcardTestResult result) {
        learningLanguage = result.getLearningLanguage();
        uiLanguage = result.getUiLanguage();
        flashcardResults = Stream.of(result.getWordResults()).map(FlashcardResultModel::new)
                .collect(Collectors.toList());
    }

    private static class FlashcardResultModel {

        @SerializedName("id")
        String id;
        @SerializedName("count")
        int count = 1;
        @SerializedName("correct")
        int correct;

        private FlashcardResultModel(FlashcardWordResult result) {
            id = result.getId();
            correct = result.isCorrect() ? 1 : 0;
        }

    }
}
