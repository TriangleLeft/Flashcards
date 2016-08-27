package com.triangleleft.flashcards.ui.main;

import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.ui.common.view.IView;

import java.util.List;

public interface IDrawerView extends IView {

    void showUserData(String username, String avatar, List<Language> languages);

    void notifyLanguageSwitchError();

    void showDrawerProgress();

}
