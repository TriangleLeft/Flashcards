package com.triangleleft.flashcards.mvp.cards;

import com.triangleleft.flashcards.service.cards.IFlashcardTestData;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.service.cards.stub.StubFlashcardTestData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

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
        Observable<IFlashcardTestData> observable = Observable.empty();
        observable = observable.doOnUnsubscribe(() -> unsubscribed.set(true));
        when(module.getFlashcards()).thenReturn(observable);

        // Simulate list load
        presenter.onLoadFlashcards();
        presenter.onDestroy();

        assertTrue(unsubscribed.get());
    }

    @Test
    public void onBindWouldStartLoadingFlashcards() {
        when(module.getFlashcards()).thenReturn(Observable.empty());

        presenter.onBind(view);

        verify(module).getFlashcards();
        verify(view).showProgress();
    }

    @Test
    public void whenHasListOnBindWouldShowList() {
        List<IFlashcardWord> list = Collections.singletonList(mock(IFlashcardWord.class));
        IFlashcardTestData data = StubFlashcardTestData.create("en", "es", list);
        when(module.getFlashcards()).thenReturn(Observable.just(data));
        presenter.onBind(view);
        presenter.onUnbind();
        reset(view);

        presenter.onBind(view);

        verify(view).showFlashcards(list);
    }

    @Test
    public void onFlashcardsLoadErrorWouldShowError() {
        when(module.getFlashcards()).thenReturn(Observable.error(new RuntimeException()));

        presenter.onBind(view);

        verify(view).showErrorNoContent();
    }

    @Test
    public void onCardsDepletedWouldPostResults() {
        presenter.onCardsDepleted();
    }
