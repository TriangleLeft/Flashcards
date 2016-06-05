package com.triangleleft.flashcards.mvp.main;

import com.triangleleft.flashcards.service.settings.ISettingsModule;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.schedulers.Schedulers;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    IMainView view;
    @Mock
    ISettingsModule settingsModule;

    private MainPresenter presenter;

    @Before
    public void before() {
        presenter = new MainPresenter(settingsModule, Schedulers.immediate());
    }

    @Test
    public void firstOnBindWouldOpenList() throws Exception {
        presenter.onBind(view);

        verify(view).showList();
    }

    @Test
    public void onBackPressedFromListShouldFinish() throws Exception {
        presenter.onBind(view);
        presenter.onBackPressed();

        verify(view).finish();
    }

    @Test
    public void onWordSelectedShouldShowWord() throws Exception {
        IVocabularWord word = mock(IVocabularWord.class);
        presenter.onBind(view);

        presenter.onWordSelected(word);

        verify(view).showWord(word);
    }

    @Test
    public void onBackPressedFromWordShouldReturnToList() {
        presenter.onBind(view);
        IVocabularWord word = mock(IVocabularWord.class);
        presenter.onWordSelected(word);
        reset(view);

        presenter.onBackPressed();

        verify(view).showList();
    }
}