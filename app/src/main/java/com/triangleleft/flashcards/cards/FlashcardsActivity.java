package com.triangleleft.flashcards.cards;

import com.daprlabs.cardstack.SwipeDeck;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.BaseActivity;
import com.triangleleft.flashcards.mvp.cards.FlashcardsPresenter;
import com.triangleleft.flashcards.mvp.cards.IFlashcardsView;
import com.triangleleft.flashcards.cards.di.CardsComponent;
import com.triangleleft.flashcards.mvp.cards.di.DaggerCardsComponent;
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
    private static final int END = 2;

    @Bind(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @Bind(R.id.swipe_deck)
    SwipeDeck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);
        ButterKnife.bind(this);

        deck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {

            }

            @Override
            public void cardSwipedRight(int position) {

            }

            @Override
            public void cardsDepleted() {
                viewFlipper.setDisplayedChild(END);
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

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
        deck.setAdapter(new FlashcardsAdapter(this, result));
        viewFlipper.setDisplayedChild(CARDS);
    }

    @Override
    public void showProgress() {
        viewFlipper.setDisplayedChild(PROGRESS);
    }

    @OnClick(R.id.restart_button)
    public void onRestartClick() {
        getPresenter().onRestartClick();
    }


}
