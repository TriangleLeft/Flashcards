package com.triangleleft.flashcards.cards;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FlashcardViewHolder {

    private final View view;
    @Bind(R.id.flashcard_word)
    TextView wordView;
    @Bind(R.id.flashcard_translation)
    TextView translationView;

    public FlashcardViewHolder(View view) {
        this.view = view;
        ButterKnife.bind(this, view);
    }

    public void showFlashcard(IFlashcardWord word) {
        wordView.setText(word.getWord());
        translationView.setText(word.getTranslation());
    }

    public void setOnClickListener(View.OnClickListener listener) {
        view.setOnClickListener((clickedView) -> {
            translationView.animate().alpha(1f);
            listener.onClick(clickedView);
        });
    }
}
