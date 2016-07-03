package com.triangleleft.flashcards.vocabular;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.common.OnItemClickListener;
import com.triangleleft.flashcards.mvp.vocabular.VocabularListPresenter;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

@FunctionsAreNonnullByDefault
public class VocabularAdapter extends RecyclerView.Adapter<VocabularViewHolder> {

    private final boolean selectable;
    private List<VocabularWord> list = Collections.emptyList();
    private OnItemClickListener<VocabularViewHolder> itemClickListener;
    private int selectedPosition = VocabularListPresenter.NO_POSITION;

    public VocabularAdapter(boolean selectable) {
        this.selectable = selectable;
    }

    public void setData(List<VocabularWord> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public VocabularViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_vocabular_word, parent, false);
        return new VocabularViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(VocabularViewHolder holder, int position) {
        VocabularWord word = getItem(position);
        holder.show(word, selectable && position == selectedPosition);
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

    public void setSelectedPosition(int position) {
        if (selectedPosition == position) {
            return;
        }
        // Clear old selected position
        if (selectable && selectedPosition != VocabularListPresenter.NO_POSITION) {
            notifyItemChanged(selectedPosition);
        }
        this.selectedPosition = position;
        if (selectable && position != VocabularListPresenter.NO_POSITION) {
            notifyItemChanged(position);
        }
    }
}
