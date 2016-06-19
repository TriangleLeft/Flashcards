package com.triangleleft.flashcards.service.settings.rest.model;

import com.google.gson.annotations.SerializedName;

import com.triangleleft.flashcards.service.settings.SimpleUserData;
import com.triangleleft.flashcards.service.settings.UserData;

import java.util.Collections;
import java.util.List;

public class UserDataModel {

    @SerializedName("languages")
    private List<LanguageDataModel> languages;
    @SerializedName("learning_language")
    public String learningLanguage;
    @SerializedName("ui_language")
    public String uiLanguage;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    public String email;

    public UserData toUserData() {
        return SimpleUserData.create(
                Collections.unmodifiableList(languages),
                avatar,
                username,
                learningLanguage,
                uiLanguage);
    }

}
