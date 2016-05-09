package com.triangleleft.flashcards.mvp.main;

import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    IMainView view;

    private MainPresenter presenter;

    @Before
    public void before() {
        presenter = new MainPresenter();
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