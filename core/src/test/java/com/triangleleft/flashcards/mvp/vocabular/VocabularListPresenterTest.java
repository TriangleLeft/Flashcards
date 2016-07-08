package com.triangleleft.flashcards.mvp.vocabular;

import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;

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
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VocabularListPresenterTest {

    @Mock
    IVocabularModule module;
    @Mock
    IVocabularNavigator navigator;
    @Mock
    IVocabularListView view;
    private VocabularListPresenter presenter;

    @Before
    public void before() {
        presenter = new VocabularListPresenter(module, navigator, Schedulers.immediate());
    }

//    @Test
//    public void onWordSelectedWouldDelegateToNavigator() {
//        VocabularWord mockWord = mock(VocabularWord.class);
//
//        presenter.onWordSelected(mockWord);
//        verify(navigator).onWordSelected(mockWord);
//    }

    @Test
    public void onLoadListWouldStartLoadingList() {
        when(module.getVocabularWords(false)).thenReturn(Observable.empty());

        presenter.onLoadList();
        verify(module).getVocabularWords(false);
    }

    @Test
    public void onDestroyWouldUnsubscribe() {
        AtomicBoolean unsubscribed = new AtomicBoolean(false);
        // Create empty observable to notify us when it's unsubscribed from
        Observable<List<VocabularWord>> observable = Observable.empty();
        observable = observable.doOnUnsubscribe(() -> unsubscribed.set(true));
        when(module.getVocabularWords(anyBoolean())).thenReturn(observable);

        // Simulate list load
        presenter.onLoadList();
        presenter.onDestroy();

        assertTrue(unsubscribed.get());
    }

    @Test
    public void onBindWouldStartLoadingList() {
        when(module.getVocabularWords(false)).thenReturn(Observable.empty());

        presenter.onBind(view);

        verify(module).getVocabularWords(false);
        verify(view).showProgress();
    }

    @Test
    public void whenHasListOnBindWouldShowList() {
        List<VocabularWord> list = Collections.singletonList(mock(VocabularWord.class));
        when(module.getVocabularWords(false)).thenReturn(Observable.just(list));
        presenter.onBind(view);
        presenter.onUnbind();
        reset(view);

        presenter.onBind(view);

        //verify(view).showWords(list, );
    }

    @Test
    public void onRefreshListWouldStartLoadingList() {
        when(module.getVocabularWords(true)).thenReturn(Observable.empty());

        presenter.onRefreshList();

        verify(module).getVocabularWords(true);
    }

    @Test
    public void onListLoadErrorWouldShowError() {
        when(module.getVocabularWords(false)).thenReturn(Observable.error(new RuntimeException()));

        presenter.onBind(view);

        verify(view).showErrorNoContent();
    }

    @Test
    public void onListRefreshWouldShowError() {
        when(module.getVocabularWords(false)).thenReturn(Observable.empty());
        presenter.onBind(view);
        reset(view);
        when(module.getVocabularWords(true)).thenReturn(Observable.error(new RuntimeException()));

        presenter.onRefreshList();

        verify(view).showError();
    }


}