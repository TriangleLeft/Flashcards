package com.triangleleft.flashcards.service.settings.rest.model;

import com.google.gson.annotations.SerializedName;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
public class SwitchLanguageController {

    @SerializedName("learning_language")
    private String languageId;

    public SwitchLanguageController(String languageId) {
        this.languageId = languageId;
    }
}
