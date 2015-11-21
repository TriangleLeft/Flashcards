package com.triangleleft.flashcards.vocab;

import com.triangleleft.flashcards.OnItemClickListener;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.IVocabularWord;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VocabularViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.vocabular_card_text)
    TextView textView;

    public VocabularViewHolder(final View itemView,
                               final OnItemClickListener<VocabularViewHolder> itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(view -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(VocabularViewHolder.this, getAdapterPosition());
            }
        });

    }

    public void show(IVocabularWord word) {
        textView.setText(word.getWord());
    }
}
