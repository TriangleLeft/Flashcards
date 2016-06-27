package com.triangleleft.flashcards.mvp.vocabular;

import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.SimpleVocabularData;
import com.triangleleft.flashcards.service.vocabular.VocabularData;
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
import static org.mockito.Mockito.*;

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

    @Test
    public void onWordSelectedWouldDelegateToNavigator() {
        VocabularWord mockWord = mock(VocabularWord.class);

        presenter.onWordSelected(mockWord);
        verify(navigator).onWordSelected(mockWord);
    }

    @Test
    public void onLoadListWouldStartLoadingList() {
        when(module.getVocabularData(false)).thenReturn(Observable.empty());

        presenter.onLoadList();
        verify(module).getVocabularData(false);
    }

    @Test
    public void onDestroyWouldUnsubscribe() {
        AtomicBoolean unsubscribed = new AtomicBoolean(false);
        // Create empty observable to notify us when it's unsubscribed from
        Observable<VocabularData> observable = Observable.empty();
        observable = observable.doOnUnsubscribe(() -> unsubscribed.set(true));
        when(module.getVocabularData(anyBoolean())).thenReturn(observable);

        // Simulate list load
        presenter.onLoadList();
        presenter.onDestroy();

        assertTrue(unsubscribed.get());
    }

    @Test
    public void onBindWouldStartLoadingList() {
        when(module.getVocabularData(false)).thenReturn(Observable.empty());

        presenter.onBind(view);

        verify(module).getVocabularData(false);
        verify(view).showProgress();
    }

    @Test
    public void whenHasListOnBindWouldShowList() {
        List<VocabularWord> list = Collections.singletonList(mock(VocabularWord.class));
        VocabularData data = SimpleVocabularData.create(list, "en", "es");
        when(module.getVocabularData(false)).thenReturn(Observable.just(data));
        presenter.onBind(view);
        presenter.onUnbind();
        reset(view);

        presenter.onBind(view);

        verify(view).showWords(list, );
    }

    @Test
    public void onRefreshListWouldStartLoadingList() {
        when(module.getVocabularData(true)).thenReturn(Observable.empty());

        presenter.onRefreshList();

        verify(module).getVocabularData(true);
    }

    @Test
    public void onListLoadErrorWouldShowError() {
        when(module.getVocabularData(false)).thenReturn(Observable.error(new RuntimeException()));

        presenter.onBind(view);

        verify(view).showErrorNoContent();
    }

    @Test
    public void onListRefreshWouldShowError() {
        when(module.getVocabularData(false)).thenReturn(Observable.empty());
        presenter.onBind(view);
        reset(view);
        when(module.getVocabularData(true)).thenReturn(Observable.error(new RuntimeException()));

        presenter.onRefreshList();

        verify(view).showError();
    }


}