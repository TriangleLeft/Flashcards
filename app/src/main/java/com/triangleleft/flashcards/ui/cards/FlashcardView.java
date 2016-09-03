package com.triangleleft.flashcards.ui.cards;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.cards.FlashcardWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FlashcardView extends FrameLayout {

    private static final Logger logger = LoggerFactory.getLogger(FlashcardView.class);

    private static final float CARD_GROWTH_START = 0.8f;
    private static final long CARD_GROWTH_DURATION = 200;

    @Bind(R.id.flashcard_word)
    TextView wordView;
    @Bind(R.id.flashcard_translation)
    TextView translationView;
    @Bind(R.id.button_right)
    Button buttonRight;
    @Bind(R.id.button_wrong)
    Button buttonWrong;
    @Bind(R.id.flashcard_fake_word)
    View fakeWord;
    @Bind(R.id.button_block)
    View buttonBlock;
    @Bind(R.id.flashcard_answer_block)
    View answerBlock;

    private boolean revealed;
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

        // setOnClickListener((view) -> reveal());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        buttonRight.setEnabled(enabled);
        buttonWrong.setEnabled(enabled);
    }

    private void reveal() {
        if (revealed) {
            return;
        }
        revealed = true;
        // setOnClickListener(null);
        TimeInterpolator interpolator = new AccelerateDecelerateInterpolator();
        // Move word up
        int diff = fakeWord.getTop() - wordView.getTop();
        wordView.animate().setInterpolator(interpolator).translationYBy(diff).setDuration(300);

        // Reveal buttons and translation
        buttonBlock.animate().setInterpolator(interpolator).alpha(1f).setDuration(300).setStartDelay(100);
        answerBlock.animate().setInterpolator(interpolator).alpha(1f).setDuration(300).setStartDelay(100);
    }


    public void showFlashcard(FlashcardWord word) {
        wordView.setText(word.getWord());
        translationView.setText(word.getTranslation());
        answerBlock.clearAnimation();
        buttonBlock.clearAnimation();
        wordView.clearAnimation();
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

    /*package*/ interface IFlashcardListener {
        void onRightClick();

        void onWrongClick();
    }


}
