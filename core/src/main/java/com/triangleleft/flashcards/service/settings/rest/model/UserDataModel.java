package com.triangleleft.flashcards.service.settings.rest.model;

import com.google.gson.annotations.SerializedName;

import com.triangleleft.flashcards.service.settings.ILanguage;
import com.triangleleft.flashcards.service.settings.IUserData;

import java.util.Collections;
import java.util.List;

public class UserDataModel implements IUserData {

    @SerializedName("languages")
    private List<LanguageDataModel> languages;
    @SerializedName("learning_language")
    public String learning_language;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    public String email;

    @Override
    public List<ILanguage> getLanguages() {
        return Collections.unmodifiableList(languages);
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
