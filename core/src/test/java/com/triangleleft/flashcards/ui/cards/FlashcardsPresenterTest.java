package com.triangleleft.flashcards.ui.cards;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardTestResult;
import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.service.cards.FlashcardWordResult;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(MockitoJUnitRunner.class)
public class FlashcardsPresenterTest {

    @Mock
    IFlashcardsModule module;
    @Mock
    IFlashcardsView view;
    private FlashcardsPresenter presenter;

    @Before
    public void before() {
        presenter = new FlashcardsPresenter(module, Schedulers.immediate());
    }

    @Test
    public void onLoadFlashcardsWouldStartLoadingFlashcards() {
        when(module.getFlashcards()).thenReturn(Observable.empty());

        presenter.onLoadFlashcards();
        verify(module).getFlashcards();
    }

    @Test
    public void onDestroyWouldUnsubscribe() {
        AtomicBoolean unsubscribed = new AtomicBoolean(false);
        // Create empty observable to notify us when it's unsubscribed from
        Observable<FlashcardTestData> observable = Observable.empty();
        observable = observable.doOnUnsubscribe(() -> unsubscribed.set(true));
        when(module.getFlashcards()).thenReturn(observable);

        // Simulate list load
        presenter.onLoadFlashcards();
        presenter.onDestroy();

        assertTrue(unsubscribed.get());
    }

    @Test
    public void onCreateWouldStartLoadingFlashcards() {
        when(module.getFlashcards()).thenReturn(Observable.empty());

        presenter.onCreate();
        presenter.onBind(view);

        verify(module).getFlashcards();
        verify(view).showProgress();
    }

    @Test
    public void whenHasListOnBindWouldShowList() {
        List<FlashcardWord> list = Collections.singletonList(mock(FlashcardWord.class));
        FlashcardTestData data = FlashcardTestData.create("en", "es", list);
        when(module.getFlashcards()).thenReturn(Observable.just(data));
        presenter.onCreate();
        presenter.onBind(view);
        presenter.onUnbind();
        reset(view);

        presenter.onBind(view);

        verify(view).showWords(list);
    }

    @Test
    public void onFlashcardsLoadErrorWouldShowError() {
        when(module.getFlashcards()).thenReturn(Observable.error(new RuntimeException()));

        presenter.onCreate();
        presenter.onBind(view);

        verify(view).showError();
    }

    @Test
    public void emptyWordsListWouldIsTreatedAsError() {
        FlashcardTestData data = mock(FlashcardTestData.class);
        when(data.getWords()).thenReturn(Collections.emptyList());
        when(module.getFlashcards()).thenReturn(Observable.just(data));

        presenter.onCreate();
        presenter.onBind(view);

        verify(view).showError();
    }


    @Test
    public void resultsArePosted() {
        FlashcardWord rightWord = mock(FlashcardWord.class);
        FlashcardWord wrongWord = mock(FlashcardWord.class);
        String uiLang = "uiLang";
        String learningLang = "learningLang";

        FlashcardTestData mock = mock(FlashcardTestData.class);
        when(mock.getUiLanguage()).thenReturn(uiLang);
        when(mock.getLearningLanguage()).thenReturn(learningLang);
        when(mock.getWords()).thenReturn(Arrays.asList(rightWord, wrongWord));
        when(module.getFlashcards()).thenReturn(Observable.just(mock));

        presenter.onCreate();
        presenter.onBind(view);
        presenter.onWordRight(rightWord);
        presenter.onWordWrong(wrongWord);
        presenter.onCardsDepleted();

        ArgumentCaptor<FlashcardTestResult> resultCaptor = ArgumentCaptor.forClass(FlashcardTestResult.class);
        verify(module).postResult(resultCaptor.capture());
        FlashcardTestResult result = resultCaptor.getValue();
        assertThat(result.getLearningLanguage(), equalTo(learningLang));
        assertThat(result.getUiLanguage(), equalTo(uiLang));

        FlashcardWordResult rightResult = FlashcardWordResult.create(rightWord, true);
        FlashcardWordResult wrongResult = FlashcardWordResult.create(wrongWord, false);
        assertThat(result.getWordResults(), hasItems(rightResult, wrongResult));
    }
}