package com.triangleleft.flashcards.ui.vocabular;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.triangleleft.flashcards.service.common.exception.ServerException;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(MockitoJUnitRunner.class)
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
        when(module.loadVocabularyWords()).thenReturn(Observable.error(new ServerException()));

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
}