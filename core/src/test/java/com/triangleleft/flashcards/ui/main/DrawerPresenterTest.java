package com.triangleleft.flashcards.ui.main;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@Ignore
@RunWith(JUnit4.class)
public class DrawerPresenterTest {

    @Mock
    MainPresenter mainPresenter;
    @Mock
    AccountModule accountModule;
    @Mock
    SettingsModule settingsModule;
    @Mock
    IDrawerView view;
    @Mock
    UserData userData;
    private DrawerPresenter presenter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        presenter = new DrawerPresenter(mainPresenter, accountModule, settingsModule, Schedulers.immediate());
        when(userData.getCurrentLearningLanguage()).thenReturn(Optional.empty());
        when(accountModule.getUserData()).thenReturn(Optional.of(userData));
        when(settingsModule.loadUserData()).thenReturn(Observable.empty());
    }

    @Test
    public void onlyLearningLanguagesAreUsed() {
        Language learningLanguage = Language.create("id", "name", 3, true, false);
        Language otherLanguage = Language.create("id2", "name2", 2, false, false);
        UserData data = prepareUserData(otherLanguage, learningLanguage, learningLanguage);
        when(accountModule.getUserData()).thenReturn(Optional.of(data));

        presenter.onCreate();
        presenter.onBind(view);

        verify(view).showUserData(any(), any(), eq(Arrays.asList(learningLanguage, learningLanguage)));
    }

    @Test
    public void onLanguageSelected() {
        List<Language> languages = prepareLanguages("user", "avatar");
        presenter.onCreate();
        presenter.onBind(view);
        reset(view);

        presenter.onLanguageSelected(languages.get(0));

        verify(view).showListProgress();
        verify(view).showUserData("user", "avatar", languages);
        verify(mainPresenter).onLanguageChanged(languages.get(0));
    }

    @Test
    public void onSameLanguageSelected() {
        List<Language> languages = prepareLanguages("", "");
        presenter.onCreate();
        presenter.onBind(view);
        presenter.onLanguageSelected(languages.get(0));
        reset(view);
        reset(mainPresenter);

        presenter.onLanguageSelected(languages.get(0));

        verify(view, never()).showListProgress();
        verify(view, never()).showUserData(any(), any(), any());
        verify(mainPresenter, never()).onLanguageChanged(any());
    }

    @Test
    public void onLanguageSelectedSwitchError() {
        Language language = mock(Language.class);
        when(settingsModule.switchLanguage(language)).thenReturn(Observable.error(new RuntimeException()));
        presenter.onCreate();
        presenter.onBind(view);

        presenter.onLanguageSelected(language);

        verify(view).notifyLanguageSwitchError();
    }

    @Test
    public void onLanguageSelectedUserDataError() {
        Language language = mock(Language.class);
        when(settingsModule.switchLanguage(language)).thenReturn(Observable.just(null));
        when(settingsModule.loadUserData()).thenReturn(Observable.error(new RuntimeException()));
        presenter.onCreate();
        presenter.onBind(view);

        presenter.onLanguageSelected(language);

        verify(view).notifyLanguageSwitchError();
    }

    private List<Language> prepareLanguages(String userName, String avatar) {
        Language language = Language.create("id", "lang", 1, true, false);
        when(settingsModule.switchLanguage(language)).thenReturn(Observable.just(null));
        List<Language> result = Collections.singletonList(language);

        // Same language, but with isCurrentlyLearning
        language = Language.create("id", "lang", 1, true, true);
        UserData data = UserData.create(Collections.singletonList(language), avatar, userName, "ui", "learn");
        when(settingsModule.loadUserData()).thenReturn(Observable.just(data));
        return result;
    }

    private UserData prepareUserData(Language... languages) {
        return UserData.create(Arrays.asList(languages), "", "", "", "");
    }
}
