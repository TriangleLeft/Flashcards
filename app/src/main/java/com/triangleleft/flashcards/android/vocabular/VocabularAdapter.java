package com.triangleleft.flashcards.android.vocabular;

import com.triangleleft.flashcards.OnItemClickListener;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class VocabularAdapter extends RecyclerView.Adapter<VocabularViewHolder> {

    private List<IVocabularWord> list;
    private OnItemClickListener<VocabularViewHolder> itemClickListener;

    public void setData(List<IVocabularWord> list) {
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
        IVocabularWord word = getItem(position);
        holder.show(word);
    }
    public IVocabularWord getItem(int position) {
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
