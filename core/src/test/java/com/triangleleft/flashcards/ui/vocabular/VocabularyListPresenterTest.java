package com.triangleleft.flashcards.ui.vocabular;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.common.exception.ServerException;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class VocabularyListPresenterTest {

    @Mock
    VocabularyModule module;
    @Mock
    VocabularyNavigator navigator;
    @Mock
    IVocabularyListView view;
    private VocabularyListPresenter presenter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        presenter = new VocabularyListPresenter(module, navigator, Schedulers.immediate());
    }

    @Test
    public void onLoadListWouldStartLoadingList() {
        when(module.loadVocabularyWords()).thenReturn(Observable.empty());

        presenter.onLoadList();
        verify(module).loadVocabularyWords();
    }

    @Test
    public void onDestroyWouldUnsubscribe() {
        AtomicBoolean unsubscribed = new AtomicBoolean(false);
        // Create empty observable to notify us when it's unsubscribed from
        Observable<List<VocabularyWord>> observable = Observable.empty();
        observable = observable.doOnUnsubscribe(() -> unsubscribed.set(true));
        when(module.loadVocabularyWords()).thenReturn(observable);

        // Simulate list load
        presenter.onLoadList();
        presenter.onDestroy();

        assertTrue(unsubscribed.get());
    }

    @Test
    public void onCreateWouldStartLoadingList() {
        when(module.loadVocabularyWords()).thenReturn(Observable.empty());

        presenter.onCreate();
        presenter.onBind(view);

        verify(module).loadVocabularyWords();
        verify(view).showProgress();
    }

    @Test
    public void onRefreshListWouldStartLoadingList() {
        when(module.refreshVocabularyWords()).thenReturn(Observable.empty());

        presenter.onRefreshList();

        verify(module).refreshVocabularyWords();
    }

    @Test
    public void onListLoadErrorWouldShowError() {
        when(module.loadVocabularyWords()).thenReturn(Observable.error(new ServerException(new Throwable())));

        presenter.onCreate();
        presenter.onBind(view);

        verify(view).showLoadError();
    }

    @Test
    public void onListRefreshWouldShowError() {
        when(module.loadVocabularyWords()).thenReturn(Observable.empty());
        presenter.onBind(view);
        reset(view);
        when(module.refreshVocabularyWords()).thenReturn(Observable.error(new RuntimeException()));

        presenter.onRefreshList();

        verify(view).showRefreshError();
    }

    @Test
    public void onLoadEmptyList() {
        when(module.loadVocabularyWords()).thenReturn(Observable.just(Collections.emptyList()));

        presenter.onCreate();
        presenter.onBind(view);

        verify(view).showEmpty();
        verify(navigator).showWord(Optional.empty());
    }

    @Test
    public void onLoadList() {
        List<VocabularyWord> list = Collections.singletonList(mock(VocabularyWord.class));
        when(module.loadVocabularyWords()).thenReturn(Observable.just(list));

        presenter.onCreate();
        presenter.onBind(view);

        verify(view).showWords(list, 0);
    }

    @Test
    public void onRefreshListPositionWithinBounds() {
        List<VocabularyWord> list = Arrays.asList(
            mock(VocabularyWord.class),
            mock(VocabularyWord.class)
        );
        when(module.loadVocabularyWords()).thenReturn(Observable.just(list));
        when(module.refreshVocabularyWords()).thenReturn(Observable.just(list));
        presenter.onCreate();
        presenter.onBind(view);
        presenter.onWordSelected(1);

        presenter.onRefreshList();

        verify(view).showWords(list, 1);
    }

    @Test
    public void onRefreshListPositionOutsideOfBounds() {
        when(module.loadVocabularyWords()).thenReturn(Observable.just(Arrays.asList(
            mock(VocabularyWord.class),
            mock(VocabularyWord.class))
        ));
        List<VocabularyWord> list = Collections.singletonList(mock(VocabularyWord.class));
        when(module.refreshVocabularyWords()).thenReturn(Observable.just(list));
        presenter.onCreate();
        presenter.onBind(view);
        presenter.onWordSelected(1);

        presenter.onRefreshList();

        verify(view).showWords(list, 0);
    }

    @Test
    public void onWordSelectedWouldDelegateToNavigator() {
        VocabularyWord secondWord = mock(VocabularyWord.class);
        when(module.loadVocabularyWords()).thenReturn(Observable.just(Arrays.asList(
            mock(VocabularyWord.class),
            secondWord
        )));
        presenter.onCreate();
        presenter.onBind(view);

        presenter.onWordSelected(1);

        verify(navigator).showWord(Optional.of(secondWord));
    }

}