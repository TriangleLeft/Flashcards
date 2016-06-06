package com.triangleleft.flashcards.cards.view;

import com.triangleleft.flashcards.service.cards.IFlashcardTestData;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

public class DeckView extends FrameLayout {

    private static final Logger logger = LoggerFactory.getLogger(DeckView.class);

    private static final long CARD_MOVE_DURATION = 300;

    private final FlashcardListener flashcardListener = new FlashcardListener();
    private FlashcardView topCard;
    private FlashcardView bottomCard;
    private int currentPosition;
    private DeckListener listener;
    private List<IFlashcardWord> list;


    public DeckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        topCard = new FlashcardView(getContext());
        bottomCard = new FlashcardView(getContext());

        setClipChildren(false);
        topCard.setListener(flashcardListener);
        bottomCard.setListener(flashcardListener);

        // Bottom card should start hidden
        bottomCard.setAlpha(0);
        addView(bottomCard, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(topCard, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setTestData(IFlashcardTestData testData) {
        this.list = testData.getWords();
        if (list.size() == 0) {
            throw new IllegalArgumentException("Non-empty list expected");
        }
        currentPosition = 0;
        // We don't need growth animations for first card
        topCard.animateGrowth().setDuration(0);
        bottomCard.setAlpha(0);
        topCard.showFlashcard(list.get(currentPosition));
    }

    private void showNextCard() {
        logger.debug("showNextCard() called " + currentPosition);
        currentPosition++;

        FlashcardView cardView = topCard;
        topCard = bottomCard;
        bottomCard = cardView;

        topCard.showFlashcard(list.get(currentPosition));
        topCard.animateGrowth();
    }

    public void setListener(DeckListener listener) {
        this.listener = listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        topCard.setEnabled(enabled);
        bottomCard.setEnabled(enabled);
    }

    public interface DeckListener {
        void onRight(IFlashcardWord word);

        void onWrong(IFlashcardWord word);

        void onDepleted();
    }

    private class FlashcardListener implements FlashcardView.IFlashcardListener {


        @Override
        public void onRightClick() {
            if (listener != null) {
                listener.onRight(list.get(currentPosition));
            }
            moveCard(topCard, Direction.RIGHT);
        }

        @Override
        public void onWrongClick() {
            if (listener != null) {
                listener.onWrong(list.get(currentPosition));
            }
            moveCard(topCard, Direction.LEFT);
        }
    }

    private void moveCard(View card, Direction direction) {
        ObjectAnimator animator;
        switch (direction) {
            case LEFT:
                animator = ObjectAnimator.ofFloat(card, "translationX", 0f, -1 * getWidth());
                break;
            case RIGHT:
                animator = ObjectAnimator.ofFloat(card, "translationX", 0f, getWidth());
                break;
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }
        animator.setDuration(CARD_MOVE_DURATION);
        final boolean hasMoreCards = currentPosition < list.size() - 1;
        animator.addListener(new AnimatorStartEndListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // If there are more cards to show, start showing them immediately
                if (hasMoreCards) {
                    showNextCard();
                }
                setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!hasMoreCards) {
                    listener.onDepleted();
                }
                setEnabled(true);
            }
        });
        animator.start();
    }

    private enum Direction {
        LEFT,
        RIGHT
    }

}
