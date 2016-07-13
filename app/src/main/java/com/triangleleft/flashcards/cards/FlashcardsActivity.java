package com.triangleleft.flashcards.cards;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ViewFlipper;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.cards.di.CardsComponent;
import com.triangleleft.flashcards.cards.di.DaggerCardsComponent;
import com.triangleleft.flashcards.cards.view.DeckView;
import com.triangleleft.flashcards.common.BaseActivity;
import com.triangleleft.flashcards.mvp.cards.FlashcardsPresenter;
import com.triangleleft.flashcards.mvp.cards.IFlashcardsView;
import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@FunctionsAreNonnullByDefault
public class FlashcardsActivity extends BaseActivity<CardsComponent, IFlashcardsView, FlashcardsPresenter> implements
        IFlashcardsView {

    private static final int PROGRESS = 0;
    private static final int CARDS = 1;
    private static final int RESULT_NO_ERRORS = 2;
    private static final int RESULT_ERRORS = 3;
    private static final int ERROR = 4;

    @Bind(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @Bind(R.id.deckView)
    DeckView deckView;
    @Bind(R.id.flashcard_result_errors_list)
    RecyclerView resultErrorList;
    @Bind(R.id.flashcard_button_container)
    View buttonContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);
        ButterKnife.bind(this);

        deckView.setListener(new DeckView.DeckListener() {
            @Override
            public void onRight(FlashcardWord word) {
                getPresenter().onWordRight(word);
            }

            @Override
            public void onWrong(FlashcardWord word) {
                getPresenter().onWordWrong(word);
            }

            @Override
            public void onDepleted() {
                getPresenter().onCardsDepleted();
            }
        });

        resultErrorList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @NonNull
    @Override
    protected CardsComponent buildComponent() {
        return DaggerCardsComponent.builder().applicationComponent(getApplicationComponent()).build();
    }

    @NonNull
    @Override
    protected IFlashcardsView getMvpView() {
        return this;
    }

    @Override
    public void showWords(List<FlashcardWord> wordList) {
        deckView.setTestData(wordList);
        viewFlipper.setDisplayedChild(CARDS);
        buttonContainer.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        viewFlipper.setDisplayedChild(PROGRESS);
        buttonContainer.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        viewFlipper.setDisplayedChild(ERROR);
        buttonContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResultsNoErrors() {
        viewFlipper.setDisplayedChild(RESULT_NO_ERRORS);
        buttonContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResultErrors(List<FlashcardWord> wordList) {
        viewFlipper.setDisplayedChild(RESULT_ERRORS);
        ResultErrorWordsAdapter adapter = new ResultErrorWordsAdapter(wordList);
        resultErrorList.setAdapter(adapter);
        buttonContainer.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.button_retry, R.id.flashcard_button_restart})
    public void onRetryClick() {
        getPresenter().onLoadFlashcards();
    }

    @OnClick(R.id.flashcard_button_back)
    public void onBackClick() {
        onBackPressed();
    }
}
