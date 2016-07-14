package com.triangleleft.flashcards.vocabular;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.common.OnItemClickListener;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import butterknife.Bind;
import butterknife.ButterKnife;

@FunctionsAreNonnullByDefault
public class VocabularyViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.item_vocabular_selected_mark)
    View selectedMark;
    @Bind(R.id.item_vocabular_text)
    TextView textView;
    @Bind(R.id.item_vocabular_strength)
    VocabularyStrengthView strengthView;

    public VocabularyViewHolder(View itemView, @Nullable OnItemClickListener<VocabularyViewHolder> itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if (itemClickListener != null) {
            itemView.setOnClickListener(
                    view -> itemClickListener.onItemClick(VocabularyViewHolder.this, getAdapterPosition()));
        }
    }

    public void show(VocabularyWord word, boolean isSelected) {
        textView.setText(word.getWord());
        selectedMark.setSelected(isSelected);
        strengthView.setStrength(word.getStrength());
    }
}
