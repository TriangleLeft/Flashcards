package com.triangleleft.flashcards.cards;

import com.triangleleft.flashcards.BaseActivity;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.cards.di.CardsComponent;
import com.triangleleft.flashcards.cards.di.DaggerCardsComponent;
import com.triangleleft.flashcards.cards.view.DeckView;
import com.triangleleft.flashcards.mvp.cards.FlashcardsPresenter;
import com.triangleleft.flashcards.mvp.cards.IFlashcardsView;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ViewFlipper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FlashcardsActivity extends BaseActivity<CardsComponent, IFlashcardsView, FlashcardsPresenter> implements
        IFlashcardsView {

    private static final int PROGRESS = 0;
    private static final int CARDS = 1;
    private static final int RESULT = 2;
    private static final int ERROR = 3;

    @Bind(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @Bind(R.id.deckView)
    DeckView deckView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);
        ButterKnife.bind(this);

        deckView.setListener(new DeckView.DeckListener() {
            @Override
            public void onRight(IFlashcardWord word) {
                getPresenter().onWordRight(word);
            }

            @Override
            public void onWrong(IFlashcardWord word) {
                getPresenter().onWordWrong(word);
            }

            @Override
            public void onDepleted() {
                getPresenter().onCardsDepleted();
            }
        });
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
    public void showFlashcards(List<IFlashcardWord> result) {
        deckView.setWordList(result);
        viewFlipper.setDisplayedChild(CARDS);
    }

    @Override
    public void showProgress() {
        viewFlipper.setDisplayedChild(PROGRESS);
    }

    @Override
    public void showErrorNoContent() {
        viewFlipper.setDisplayedChild(ERROR);
    }

    @Override
    public void showResults() {
        viewFlipper.setDisplayedChild(RESULT);
    }

    @OnClick(R.id.button_retry)
    public void onRetryClick() {
        getPresenter().onLoadFlashcards();
    }

    @OnClick(R.id.button_restart)
    public void onRestartClick() {
        getPresenter().onLoadFlashcards();
    }


}
