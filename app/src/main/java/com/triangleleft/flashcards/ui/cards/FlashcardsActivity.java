package com.triangleleft.flashcards.ui.cards;

import com.daprlabs.aaron.swipedeck.SwipeDeck;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.di.cards.CardsComponent;
import com.triangleleft.flashcards.di.cards.DaggerCardsComponent;
import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.ui.common.BaseActivity;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
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
    private SparseBooleanArray revealedCards = new SparseBooleanArray();

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

            @Override
            public void onRevealed() {
                revealedCards.put((int) swipeDeck.getTopCardItemId(), true);
            }
        });
        swipeDeck.setAdapter(adapter);
        swipeDeck.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long positionInAdapter) {
                FlashcardWord word = adapter.getItem((int) positionInAdapter);
                getPresenter().onWordWrong(word);
                if (positionInAdapter == adapter.getCount() - 1) {
                    getPresenter().onCardsDepleted();
                }
            }

            @Override
            public void cardSwipedRight(long positionInAdapter) {
                FlashcardWord word = adapter.getItem((int) positionInAdapter);
                getPresenter().onWordRight(word);
                if (positionInAdapter == adapter.getCount() - 1) {
                    getPresenter().onCardsDepleted();
                }
            }

            @Override
            public boolean isDragEnabled(long itemId) {
                return revealedCards.get((int) itemId);
            }

//            @Override
//            public void cardsDepleted() {
//                getPresenter().onCardsDepleted();
//            }
//
//
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
        viewFlipper.setDisplayedChild(CARDS);
        new Handler().post(() -> {
            revealedCards.clear();
            swipeDeck.setAdapterIndex(0);
            adapter.setData(wordList);
        });


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
