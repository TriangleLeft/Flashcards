package com.triangleleft.flashcards.ui.cards;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.service.cards.ReviewDirection;

import java.util.Collections;
import java.util.List;

public class SwipeDeckAdapter extends BaseAdapter {

    private final FlashcardView.IFlashcardListener listener;
    private List<FlashcardWord> words = Collections.emptyList();
    private ReviewDirection reviewDirection;

    public SwipeDeckAdapter(FlashcardView.IFlashcardListener listener) {
        this.listener = listener;
    }

    public void setData(List<FlashcardWord> words, ReviewDirection direction) {
        this.words = words;
        this.reviewDirection = direction;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public FlashcardWord getItem(int position) {
        return words.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FlashcardView view = (FlashcardView) convertView;
        if (view == null) {
            view = new FlashcardView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            view.setListener(listener);
        }

        view.showFlashcard(getItem(position), reviewDirection);
        return view;
    }

}
