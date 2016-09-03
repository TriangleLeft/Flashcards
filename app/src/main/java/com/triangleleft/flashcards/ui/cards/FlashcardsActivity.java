package com.triangleleft.flashcards.ui.cards;

import com.daprlabs.cardstack.SwipeDeck;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.di.cards.CardsComponent;
import com.triangleleft.flashcards.di.cards.DaggerCardsComponent;
import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.ui.common.BaseActivity;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ViewFlipper;

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
    @Bind(R.id.swipe_deck)
    SwipeDeck swipeDeck;
    @Bind(R.id.flashcard_result_errors_list)
    RecyclerView resultErrorList;
    @Bind(R.id.flashcard_button_container)
    View buttonContainer;
    private SwipeDeckAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);
        ButterKnife.bind(this);

        adapter = new SwipeDeckAdapter(new FlashcardView.IFlashcardListener() {
            @Override
            public void onRightClick() {
                swipeDeck.swipeTopCardRight(100);
            }

            @Override
            public void onWrongClick() {
                swipeDeck.swipeTopCardLeft(100);
            }
        });
        swipeDeck.setAdapter(adapter);

        swipeDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int positionInAdapter) {
                FlashcardWord word = adapter.getItem(positionInAdapter);
                getPresenter().onWordWrong(word);
            }

            @Override
            public void cardSwipedRight(int positionInAdapter) {
                FlashcardWord word = adapter.getItem(positionInAdapter);
                getPresenter().onWordRight(word);
            }

            @Override
            public void cardsDepleted() {
                getPresenter().onCardsDepleted();
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

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
        return DaggerCardsComponent.builder().androidApplicationComponent(getApplicationComponent()).build();
    }

    @NonNull
    @Override
    protected IFlashcardsView getMvpView() {
        return this;
    }

    @Override
    public void showWords(List<FlashcardWord> wordList) {
        adapter.setData(wordList);
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
