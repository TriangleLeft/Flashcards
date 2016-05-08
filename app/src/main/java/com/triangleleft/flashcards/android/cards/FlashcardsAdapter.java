package com.triangleleft.flashcards.android.cards;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class FlashcardsAdapter extends BaseAdapter {

    private final Context context;
    private final List<IFlashcardWord> list;

    public FlashcardsAdapter(Context context, List<IFlashcardWord> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.view_flashcard, parent, false);
            FlashcardViewHolder viewHolder = new FlashcardViewHolder(view);
            view.setTag(viewHolder);
        }
        FlashcardViewHolder viewHolder = (FlashcardViewHolder) view.getTag();
        viewHolder.showFlashcard((IFlashcardWord) getItem(position));

        return view;
    }
}
