package com.triangleleft.flashcards.ui.main;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.UserData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MainPresenterTest {

    @Mock
    AccountModule accountModule;
    @Mock
    IMainView view;
    @Mock
    UserData userData;

    private MainPresenter presenter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        presenter = new MainPresenter(accountModule, null);
        when(accountModule.getUserData()).thenReturn(Optional.of(userData));
        //when(userData.getCurrentLearningLanguage()).thenReturn(Optional.empty());
    }

    @Test
    public void firstOnBindWouldOpenList() throws Exception {
//        presenter.onCreate();
//        presenter.onBind(view);
//
//        verify(view).showList();
    }

//    @Test
//    public void currentLearningLanguageTitle() {
//        Language stubLanguage = Language.create("", "title", 1, true, true);
//        when(userData.getCurrentLearningLanguage()).thenReturn(Optional.of(stubLanguage));
//
//        presenter.onCreate();
//        presenter.onBind(view);
//
//        verify(view).setTitle("title");
//    }
//
//    @Test
//    public void onBackPressedFromListShouldFinish() throws Exception {
//        presenter.onCreate();
//        presenter.onBind(view);
//        presenter.onBackPressed();
//
//        verify(view).finish();
//    }
//
//    @Test
//    public void onWordSelectedShouldShowWord() throws Exception {
//        Optional<VocabularyWord> word = Optional.of(mock(VocabularyWord.class));
//        presenter.onCreate();
//        presenter.onBind(view);
//
//        presenter.showWord(word);
//
//        verify(view).showWord(word);
//    }
//
//    @Test
//    public void onBackPressedFromWordShouldReturnToList() {
//        Optional<VocabularyWord> word = Optional.of(mock(VocabularyWord.class));
//        presenter.onCreate();
//        presenter.onBind(view);
//        presenter.showWord(word);
//        reset(view);
//
//        presenter.onBackPressed();
//
//        verify(view).showList();
//    }

}