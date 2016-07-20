package com.triangleleft.flashcards.service.settings.rest.model;

import static com.annimon.stream.Collectors.toList;
import static com.triangleleft.flashcards.util.ListUtils.wrapList;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.google.gson.annotations.SerializedName;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.UserData;

import java.util.List;

public class UserDataModel {

    @SerializedName("learning_language")
    public String learningLanguage;
    @SerializedName("ui_language")
    public String uiLanguage;
    @SerializedName("email")
    public String email;
    @SerializedName("languages")
    private List<LanguageDataModel> languages;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("username")
    private String username;

    public UserData toUserData() {
        List<Language> list = Stream.of(wrapList(languages))
            .map(LanguageDataModel::toLanguage)
            .collect(toList());
        // Try to Apply avatar fix
        avatar = avatar == null ? null : "https:" + avatar + "/large";
        return UserData.create(
            list,
            Optional.ofNullable(avatar),
            Optional.ofNullable(username),
            uiLanguage,
            learningLanguage);
    }

}
