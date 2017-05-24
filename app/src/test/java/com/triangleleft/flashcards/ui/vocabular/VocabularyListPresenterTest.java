package com.triangleleft.flashcards.ui.vocabular;

import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

@RunWith(JUnit4.class)
public class VocabularyListPresenterTest {

    @Mock
    VocabularyModule module;
    @Mock
    VocabularyNavigator navigator;
    @Mock
    VocabularyListView view;
    @Mock
    Call<List<VocabularyWord>> mockCall;
    private VocabularyListPresenter presenter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        presenter = new VocabularyListPresenter(module, navigator, null);
    }

    @Test
    public void onLoadListWouldStartLoadingList() {
//        when(module.loadVocabularyWords()).thenReturn(Call.empty());
//
//        presenter.onLoadList();
//        verify(module).loadVocabularyWords();
    }
//
//    @Ignore
//    @Test
//    public void onDestroyWouldCancel() {
//        AtomicBoolean canceled = new AtomicBoolean(false);
//        // Create empty observable to notify us when it's canceled from
//        doAnswer(invocation -> {
//            canceled.set(true);
//            return true;
//        }).when(mockCall).cancel();
//        when(module.loadVocabularyWords()).thenReturn(mockCall);
//
//        // Simulate list load
//        presenter.onLoadList();
//        presenter.onDestroy();
//
//        assertThat(canceled.get(), is(true));
//    }
//
//    @Test
//    public void onCreateWouldStartLoadingList() {
//        when(module.loadVocabularyWords()).thenReturn(Call.empty());
//
//        presenter.onCreate();
//        presenter.onBind(view);
//
//        verify(module).loadVocabularyWords();
//        verify(view).showProgress();
//    }
//
//    @Test
//    public void onRefreshListWouldStartLoadingList() {
//        when(module.refreshVocabularyWords()).thenReturn(Call.empty());
//
//        presenter.onRefreshList();
//
//        verify(module).refreshVocabularyWords();
//    }
//
//    @Test
//    public void onListLoadErrorWouldShowError() {
//        when(module.loadVocabularyWords()).thenReturn(Call.error(new ServerException()));
//
//        presenter.onCreate();
//        presenter.onBind(view);
//
//        verify(view).showLoadError();
//    }
//
//    @Test
//    public void onListRefreshWouldShowError() {
//        when(module.loadVocabularyWords()).thenReturn(Call.empty());
//        presenter.onBind(view);
//        reset(view);
//        when(module.refreshVocabularyWords()).thenReturn(Call.error(new RuntimeException()));
//
//        presenter.onRefreshList();
//
//        verify(view).showRefreshError();
//    }
//
//    @Test
//    public void onLoadEmptyList() {
//        when(module.loadVocabularyWords()).thenReturn(Call.just(Collections.emptyList()));
//
//        presenter.onCreate();
//        presenter.onBind(view);
//
//        verify(view).showEmpty();
//        verify(navigator).showWord(Optional.empty());
//    }
//
//    @Test
//    public void onLoadList() {
//        List<VocabularyWord> list = Collections.singletonList(mock(VocabularyWord.class));
//        when(module.loadVocabularyWords()).thenReturn(Call.just(list));
//
//        presenter.onCreate();
//        presenter.onBind(view);
//
//        verify(view).showWords(list, 0);
//    }
//
//    @Test
//    public void onRefreshListPositionWithinBounds() {
//        List<VocabularyWord> list = Arrays.asList(
//                mock(VocabularyWord.class),
//                mock(VocabularyWord.class)
//        );
//        when(module.loadVocabularyWords()).thenReturn(Call.just(list));
//        when(module.refreshVocabularyWords()).thenReturn(Call.just(list));
//        presenter.onCreate();
//        presenter.onBind(view);
//        presenter.onWordSelected(1);
//
//        presenter.onRefreshList();
//
//        verify(view).showWords(list, 1);
//    }
//
//    @Test
//    public void onRefreshListPositionOutsideOfBounds() {
//        when(module.loadVocabularyWords()).thenReturn(Call.just(Arrays.asList(
//                mock(VocabularyWord.class),
//                mock(VocabularyWord.class))
//        ));
//        List<VocabularyWord> list = Collections.singletonList(mock(VocabularyWord.class));
//        when(module.refreshVocabularyWords()).thenReturn(Call.just(list));
//        presenter.onCreate();
//        presenter.onBind(view);
//        presenter.onWordSelected(1);
//
//        presenter.onRefreshList();
//
//        verify(view).showWords(list, 0);
//    }
//
//    @Test
//    public void onWordSelectedWouldDelegateToNavigator() {
//        VocabularyWord secondWord = mock(VocabularyWord.class);
//        when(module.loadVocabularyWords()).thenReturn(Call.just(Arrays.asList(
//                mock(VocabularyWord.class),
//                secondWord
//        )));
//        presenter.onCreate();
//        presenter.onBind(view);
//
//        presenter.onWordSelected(1);
//
//        verify(navigator).showWord(Optional.of(secondWord));
//    }

}