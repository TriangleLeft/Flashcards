package com.triangleleft.flashcards.ui.cards;

import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.service.cards.FlashcardsModule;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

//import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

@Ignore
@RunWith(JUnit4.class)
public class FlashcardsPresenterTest {

    private static final String UI_LANG = "ui";
    private static final String LEARN_LANG = "learn";

    @Mock
    FlashcardsModule module;
    @Mock
    IFlashcardsView view;
    @Captor
    ArgumentCaptor<List<FlashcardWord>> listCaptor;

    private FlashcardsPresenter presenter;

//    @Before
//    public void before() {
//        MockitoAnnotations.initMocks(this);
//        presenter = new FlashcardsPresenter(module, Schedulers.immediate());
//    }
//
//    @Test
//    public void onLoadFlashcardsWouldStartLoadingFlashcards() {
//        when(module.getFlashcards()).thenReturn(Observable.empty());
//
//        presenter.onLoadFlashcards();
//        verify(module).getFlashcards();
//    }
//
//    @Test
//    public void onDestroyWouldUnsubscribe() {
//        AtomicBoolean unsubscribed = new AtomicBoolean(false);
//        // Create empty observable to notify us when it's unsubscribed from
//        Observable<FlashcardTestData> observable = Observable.empty();
//        observable = observable.doOnUnsubscribe(() -> unsubscribed.set(true));
//        when(module.getFlashcards()).thenReturn(observable);
//
//        // Simulate list load
//        presenter.onLoadFlashcards();
//        presenter.onDestroy();
//
//        assertTrue(unsubscribed.get());
//    }
//
//    @Test
//    public void onCreateWouldStartLoadingFlashcards() {
//        when(module.getFlashcards()).thenReturn(Observable.empty());
//
//        presenter.onCreate();
//        presenter.onBind(view);
//
//        verify(module).getFlashcards();
//        verify(view).showProgress();
//    }
//
//    @Test
//    public void whenHasListOnBindWouldShowList() {
//        List<FlashcardWord> list = Collections.singletonList(mock(FlashcardWord.class));
//        prepareTestData(list);
//        presenter.onCreate();
//        presenter.onBind(view);
//        presenter.onUnbind();
//        reset(view);
//
//        presenter.onBind(view);
//
//        verify(view).showWords(list);
//    }
//
//    @Test
//    public void onFlashcardsLoadErrorWouldShowError() {
//        when(module.getFlashcards()).thenReturn(Observable.error(new RuntimeException()));
//
//        presenter.onCreate();
//        presenter.onBind(view);
//
//        verify(view).showError();
//    }
//
//    @Test
//    public void emptyWordsListIsTreatedAsError() {
//        prepareTestData(Collections.emptyList());
//
//        presenter.onCreate();
//        presenter.onBind(view);
//
//        verify(view).showError();
//    }

//    @Test
//    public void resultsArePosted() {
//        FlashcardWord rightWord = mock(FlashcardWord.class);
//        FlashcardWord wrongWord = mock(FlashcardWord.class);
//        prepareTestData(Arrays.asList(rightWord, wrongWord));
//
//        presenter.onCreate();
//        presenter.onBind(view);
//        presenter.onWordRight(rightWord);
//        presenter.onWordWrong(wrongWord);
//        presenter.onCardsDepleted();
//
//        ArgumentCaptor<FlashcardTestResult> resultCaptor = ArgumentCaptor.forClass(FlashcardTestResult.class);
//        verify(module).postResult(resultCaptor.capture());
//        FlashcardTestResult result = resultCaptor.getValue();
//        assertThat(result.getLearningLanguage(), equalTo(LEARN_LANG));
//        assertThat(result.getUiLanguage(), equalTo(UI_LANG));
//
//        FlashcardWordResult rightResult = FlashcardWordResult.create(rightWord, true);
//        FlashcardWordResult wrongResult = FlashcardWordResult.create(wrongWord, false);
//        assertThat(result.getWordResults(), containsInAnyOrder(rightResult, wrongResult));
//    }

//    @Test
//    public void resultWithErrors() {
//        FlashcardWord wrongWord = mock(FlashcardWord.class);
//        prepareTestData(Collections.singletonList(wrongWord));
//
//        presenter.onCreate();
//        presenter.onBind(view);
//        presenter.onWordWrong(wrongWord);
//        presenter.onCardsDepleted();
//
//        verify(view).showResultErrors(listCaptor.capture());
//        List<FlashcardWord> list = listCaptor.getValue();
//        assertThat(list, containsInAnyOrder(wrongWord));
//    }

//    @Test
//    public void resultWithNoErrors() {
//        prepareTestData(Collections.singletonList(mock(FlashcardWord.class)));
//
//        presenter.onCreate();
//        presenter.onBind(view);
//        presenter.onCardsDepleted();
//
//        verify(view).showResultsNoErrors();
//    }
//
//    private void prepareTestData(List<FlashcardWord> words) {
//        FlashcardTestData data = mock(FlashcardTestData.class);
//        when(data.getUiLanguage()).thenReturn(UI_LANG);
//        when(data.getLearningLanguage()).thenReturn(LEARN_LANG);
//        when(data.getWords()).thenReturn(words);
//        when(module.getFlashcards()).thenReturn(Observable.just(data));
//    }
}