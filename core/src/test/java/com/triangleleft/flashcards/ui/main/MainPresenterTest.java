package com.triangleleft.flashcards.ui.main;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class MainPresenterTest {

    @Mock
    AccountModule accountModule;
    @Mock
    SettingsModule settingsModule;
    @Mock
    IMainView view;
    @Mock
    UserData userData;

    private MainPresenter presenter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        presenter = new MainPresenter(accountModule, settingsModule, Schedulers.immediate());
        when(accountModule.getUserData()).thenReturn(Optional.of(userData));
    }

    @Test
    public void firstOnBindWouldOpenList() throws Exception {
        presenter.onCreate();
        presenter.onBind(view);

        verify(view).showList();
    }

    @Test
    public void onBackPressedFromListShouldFinish() throws Exception {
        presenter.onCreate();
        presenter.onBind(view);
        presenter.onBackPressed();

        verify(view).finish();
    }

    @Test
    public void onWordSelectedShouldShowWord() throws Exception {
        Optional<VocabularyWord> word = Optional.of(mock(VocabularyWord.class));
        presenter.onCreate();
        presenter.onBind(view);

        presenter.showWord(word);

        verify(view).showWord(word);
    }

    @Test
    public void onBackPressedFromWordShouldReturnToList() {
        Optional<VocabularyWord> word = Optional.of(mock(VocabularyWord.class));
        presenter.onCreate();
        presenter.onBind(view);
        presenter.showWord(word);
        reset(view);

        presenter.onBackPressed();

        verify(view).showList();
    }

    @Test
    public void onLanguageSelected() {
        List<Language> languages = prepareLanguages("user", "avatar");
        presenter.onCreate();
        presenter.onBind(view);
        reset(view);

        presenter.onLanguageSelected(languages.get(0));

        verify(view).showDrawerProgress();
        verify(view).showUserData("user", "avatar", languages);
        verify(view).reloadList();
    }

    @Test
    public void onSameLanguageSelected() {
        List<Language> languages = prepareLanguages("", "");
        presenter.onCreate();
        presenter.onBind(view);
        presenter.onLanguageSelected(languages.get(0));
        reset(view);

        presenter.onLanguageSelected(languages.get(0));

        verify(view, never()).showDrawerProgress();
        verify(view, never()).showUserData(any(), any(), any());
        verify(view, never()).reloadList();
    }

    @Test
    public void onLanguageSelectedSwitchError() {
        Language language = mock(Language.class);
        when(settingsModule.switchLanguage(language)).thenReturn(Observable.error(new RuntimeException()));
        presenter.onCreate();
        presenter.onBind(view);

        presenter.onLanguageSelected(language);

        verify(view).showDrawerError();
    }

    @Test
    public void onLanguageSelectedUserDataError() {
        Language language = mock(Language.class);
        when(settingsModule.switchLanguage(language)).thenReturn(Observable.just(null));
        when(settingsModule.loadUserData()).thenReturn(Observable.error(new RuntimeException()));
        presenter.onCreate();
        presenter.onBind(view);

        presenter.onLanguageSelected(language);

        verify(view).showDrawerError();
    }

    @Test
    public void onlyLearningLanguagesAreUsed() {
        Language learningLanguage = Language.create("id", "name", 3, true, false);
        Language otherLanguage = Language.create("id2", "name2", 2, false, false);
        List<Language> languages = Arrays.asList(otherLanguage, learningLanguage, learningLanguage);
        UserData data = UserData.create(languages, "", "", "", "");
        when(accountModule.getUserData()).thenReturn(Optional.of(data));

        presenter.onCreate();
        presenter.onBind(view);

        verify(view).showUserData(any(), any(), eq(Arrays.asList(learningLanguage, learningLanguage)));
    }

    @Test
    public void pagesEnum() {
        assertThat(IMainView.Page.values(), is(notNullValue()));
        assertThat(IMainView.Page.WORD, equalTo(IMainView.Page.valueOf(IMainView.Page.WORD.name())));
    }

    private List<Language> prepareLanguages(String userName, String avatar) {
        Language language = mock(Language.class);
        when(language.isLearning()).thenReturn(true);
        when(settingsModule.switchLanguage(language)).thenReturn(Observable.just(null));

        List<Language> languages = Collections.singletonList(language);
        UserData data = UserData.create(languages, avatar, userName, "ui", "learn");
        when(settingsModule.loadUserData()).thenReturn(Observable.just(data));
        return languages;
    }

}