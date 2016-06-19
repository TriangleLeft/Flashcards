package com.triangleleft.flashcards.service.settings;

import java.util.List;

public interface UserData {

    List<ILanguage> getLanguages();

    String getAvatar();

    String getUsername();

    String getUiLanguageId();

    String getLearningLanguageId();
}
