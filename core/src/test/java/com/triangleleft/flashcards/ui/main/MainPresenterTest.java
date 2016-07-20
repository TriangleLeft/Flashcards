package com.triangleleft.flashcards.ui.main;

import static org.mockito.Mockito.*;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.schedulers.Schedulers;

@RunWith(MockitoJUnitRunner.class)
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
}