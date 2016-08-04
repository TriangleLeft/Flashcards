package com.triangleleft.flashcards.service.settings.rest.model;

import static com.annimon.stream.Collectors.toList;
import static com.triangleleft.flashcards.util.ListUtils.wrapList;

import com.annimon.stream.Stream;
import com.google.gson.annotations.SerializedName;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.UserData;

import java.util.List;

public class UserDataModel {

    /*package*/ final static String URL_FORMAT = "https:%s/large";

    @SerializedName("learning_language")
    String learningLanguage;
    @SerializedName("ui_language")
    String uiLanguage;
    @SerializedName("email")
    String email;
    @SerializedName("languages")
    List<LanguageDataModel> languages;
    @SerializedName("avatar")
    String avatar;
    @SerializedName("username")
    String username;

    public UserData toUserData() {
        List<Language> list = Stream.of(wrapList(languages))
            .map(LanguageDataModel::toLanguage)
            .collect(toList());
        // Avatar url is sent without scheme
        // We also need to specify avatar size, use "large" by default
        return UserData.create(
            list,
            String.format(URL_FORMAT, avatar),
            username,
            uiLanguage,
            learningLanguage);
    }

}
