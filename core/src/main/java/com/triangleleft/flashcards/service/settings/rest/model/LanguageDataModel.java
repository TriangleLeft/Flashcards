package com.triangleleft.flashcards.service.settings.rest.model;

import com.google.gson.annotations.SerializedName;

import com.triangleleft.flashcards.service.settings.ILanguage;

public class LanguageDataModel implements ILanguage {

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

    @Override
    public String getId() {
        return languageId;
    }

    @Override
    public String getName() {
        return languageString;
    }

    @Override
    public int getLevel() {
        return level == null ? 0 : level;
    }

    @Override
    public boolean isLearning() {
        return learning == null ? false : learning;
    }

    @Override
    public boolean isCurrentLearning() {
        return currentLearning == null ? false : currentLearning;
    }

}
