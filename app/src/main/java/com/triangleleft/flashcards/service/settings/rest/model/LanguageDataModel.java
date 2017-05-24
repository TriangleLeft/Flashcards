package com.triangleleft.flashcards.service.settings.rest.model;

import com.google.gson.annotations.SerializedName;
import com.triangleleft.flashcards.service.settings.Language;

public class LanguageDataModel {

    @SerializedName("language_string")
    String languageString;
    @SerializedName("learning")
    Boolean learning;
    @SerializedName("current_learning")
    Boolean currentLearning;
    @SerializedName("language")
    String languageId;
    @SerializedName("level")
    Integer level;

    public Language toLanguage() {
        return new Language(
                languageId,
                languageString,
                level,
                learning,
                currentLearning
        );
    }

}
