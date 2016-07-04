package com.triangleleft.flashcards.vocabular;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.common.OnItemClickListener;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

@FunctionsAreNonnullByDefault
public class VocabularViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.item_vocabular_selected_mark)
    View selectedMark;
    @Bind(R.id.item_vocabular_text)
    TextView textView;
    @Bind(R.id.item_vocabular_strength)
    VocabularStrengthView strengthView;

    public VocabularViewHolder(View itemView, @Nullable OnItemClickListener<VocabularViewHolder> itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if (itemClickListener != null) {
            itemView.setOnClickListener(
                    view -> itemClickListener.onItemClick(VocabularViewHolder.this, getAdapterPosition()));
        }
    }

    public void show(VocabularWord word, boolean isSelected) {
        textView.setText(word.getWord());
        selectedMark.setSelected(isSelected);
        strengthView.setStrength(word.getStrength());
    }
}
