package com.triangleleft.flashcards.ui.vocabular;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.common.OnItemClickListener;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.Collections;
import java.util.List;

@FunctionsAreNonnullByDefault
public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyViewHolder> {

    private final boolean selectable;
    private List<VocabularyWord> list = Collections.emptyList();
    private OnItemClickListener<VocabularyViewHolder> itemClickListener;
    private int selectedPosition = VocabularyListPresenter.NO_POSITION;

    public VocabularyAdapter(boolean selectable) {
        this.selectable = selectable;
    }

    public void setData(List<VocabularyWord> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public VocabularyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vocabular_word, parent, false);
        return new VocabularyViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(VocabularyViewHolder holder, int position) {
        VocabularyWord word = getItem(position);
        holder.show(word, selectable && position == selectedPosition);
    }

    public VocabularyWord getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClickListener(OnItemClickListener<VocabularyViewHolder> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setSelectedPosition(int position) {
        if (selectedPosition == position) {
            return;
        }
        // Clear old selected position
        if (selectable && selectedPosition != VocabularyListPresenter.NO_POSITION) {
            notifyItemChanged(selectedPosition);
        }
        this.selectedPosition = position;
        if (selectable && position != VocabularyListPresenter.NO_POSITION) {
            notifyItemChanged(position);
        }
    }
}
