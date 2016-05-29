package com.triangleleft.flashcards.service.settings;

import java.util.List;

public interface IUserData {

    List<ILanguage> getLanguages();

    String getAvatar();

    String getUsername();
}
