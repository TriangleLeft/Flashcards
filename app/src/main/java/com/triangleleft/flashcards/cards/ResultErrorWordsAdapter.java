/*
 *
 * =======================================================================
 *
 * Copyright (c) 2014-2015 Domlex Limited. All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Domlex Limited.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Domlex Limited.
 *
 * =======================================================================
 *
 */

package com.triangleleft.flashcards.cards;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.cards.FlashcardWord;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResultErrorWordsAdapter extends RecyclerView.Adapter<ResultErrorWordsAdapter.ViewHolder> {

    private final List<FlashcardWord> wordList;

    public ResultErrorWordsAdapter(List<FlashcardWord> wordList) {
        this.wordList = wordList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_flashcard_error_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.showWord(wordList.get(position));
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    /*package*/ static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.flashcard_result_error_word_value)
        TextView value;
        @Bind(R.id.flashcard_result_error_word_translation)
        TextView translation;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void showWord(FlashcardWord flashcardWord) {
            value.setText(flashcardWord.getWord());
            translation.setText(flashcardWord.getTranslation());
        }
    }
}
