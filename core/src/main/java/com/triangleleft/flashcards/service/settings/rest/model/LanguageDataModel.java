package com.triangleleft.flashcards.service.settings.rest.model;

import com.google.gson.annotations.SerializedName;

import com.triangleleft.flashcards.service.settings.Language;

public class LanguageDataModel {

    @SerializedName("language_string")
    private String languageString;
    @SerializedName("learning")
    private Boolean learning;
    @SerializedName("current_learning")
    private Boolean currentLearning;
    @SerializedName("language")
    private String languageId;
    @SerializedName("level")
    private Integer level;

    public Language toLanguage() {
        return Language.create(
                languageId,
                languageString,
                level == null ? 0 : level,
                learning == null ? false : learning,
                currentLearning == null ? false : currentLearning
        );
    }


}
