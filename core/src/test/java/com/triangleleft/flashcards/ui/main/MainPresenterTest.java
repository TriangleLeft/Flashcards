package com.triangleleft.flashcards.ui.main;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Ignore
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
        presenter = new MainPresenter(accountModule);
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

}