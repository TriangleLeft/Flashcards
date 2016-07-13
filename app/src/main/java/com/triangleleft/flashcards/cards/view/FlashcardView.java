package com.triangleleft.flashcards.cards.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.cards.FlashcardWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FlashcardView extends FrameLayout {

    private static final Logger logger = LoggerFactory.getLogger(FlashcardView.class);

    private final static int WORD = 0;
    private final static int TRANSLATION = 1;
    private final static long CARD_FLIP_DURATION = 300;
    private static final float CARD_GROWTH_START = 0.8f;
    private static final long CARD_GROWTH_DURATION = 200;

    @Bind(R.id.flashcard_flipper)
    ViewFlipper viewFlipper;
    @Bind(R.id.flashcard_word)
    TextView wordView;
    @Bind(R.id.flashcard_translation)
    TextView translationView;
    @Bind(R.id.button_right)
    Button buttonRight;
    @Bind(R.id.button_wrong)
    Button buttonWrong;

    private final ObjectAnimator flipper = ObjectAnimator.ofFloat(this, "rotationY", 0.0f, 180.0f);
    private boolean topFaced = true;
    private IFlashcardListener listener;

    public FlashcardView(Context context) {
        this(context, null);
    }

    public FlashcardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_flashcard, this);
        ButterKnife.bind(this);

        setOnClickListener((view) -> flip());
        flipper.setDuration(CARD_FLIP_DURATION);
        flipper.addUpdateListener(animation -> {
            float rotationY = (float) animation.getAnimatedValue();
            rotationY = Math.abs(rotationY) % 360;
            if (rotationY > 90f && rotationY < 270f) {
                showTranslation();
            } else {
                showWord();
            }

        });
        flipper.addListener(
                new AnimatorStartEndListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        setEnabled(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setLayerType(View.LAYER_TYPE_NONE, null);
                        setEnabled(true);
                    }
                });
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        buttonRight.setEnabled(enabled);
        buttonWrong.setEnabled(enabled);
    }

    private void flip() {
        if (topFaced) {
            flipper.start();
        } else {
            flipper.reverse();
        }
    }

    private void showWord() {
        topFaced = true;
        viewFlipper.setDisplayedChild(WORD);
    }

    private void showTranslation() {
        topFaced = false;
        viewFlipper.setDisplayedChild(TRANSLATION);
    }

    public void showFlashcard(FlashcardWord word) {
        showWord();
        wordView.setText(word.getWord());
        translationView.setText(word.getTranslation());
        setRotationY(0f);
        requestLayout();
        invalidate();
        // TODO: revisit
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        buildLayer();
    }

    @OnClick(R.id.button_right)
    public void onRightClick() {
        if (listener != null) {
            listener.onRightClick();
        }
    }

    @OnClick(R.id.button_wrong)
    public void onWrongClick() {
        if (listener != null) {
            listener.onWrongClick();
        }
    }

    public void setListener(IFlashcardListener listener) {
        this.listener = listener;
    }


    public ViewPropertyAnimator animateGrowth() {
        setTranslationX(0);
        setScaleX(CARD_GROWTH_START);
        setScaleY(CARD_GROWTH_START);
        setAlpha(0f);

        // "Grow" animation
        return animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(CARD_GROWTH_DURATION);
    }

    /*package*/ interface IFlashcardListener {
        void onRightClick();

        void onWrongClick();
    }


}
