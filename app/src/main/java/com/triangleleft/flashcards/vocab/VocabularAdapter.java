package com.triangleleft.flashcards.vocab;

import com.triangleleft.flashcards.OnItemClickListener;
import com.triangleleft.flashcards.R;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class VocabularAdapter extends RecyclerView.Adapter<VocabularViewHolder> {

    private List<VocabularWord> list;
    private OnItemClickListener<VocabularViewHolder> itemClickListener;

    public void setData(List<VocabularWord> list) {
        this.list = list;
    }
    
    @Override
    public VocabularViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vocabular_card, parent, false);
        return new VocabularViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(VocabularViewHolder holder, int position) {
        VocabularWord word = getItem(position);
        holder.show(word);
    }
    public VocabularWord getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClickListener(OnItemClickListener<VocabularViewHolder> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
