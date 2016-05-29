package com.triangleleft.flashcards.main;

import com.triangleleft.flashcards.OnItemClickListener;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.settings.ILanguage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

public class DrawerLanguagesAdapter extends RecyclerView.Adapter<DrawerLanguageViewHolder> {

    private List<ILanguage> languages = Collections.emptyList();
    private OnItemClickListener<DrawerLanguageViewHolder> listener;

    public DrawerLanguagesAdapter(OnItemClickListener<DrawerLanguageViewHolder> listener) {
        this.listener = listener;
    }

    public void setData(List<ILanguage> languages) {
        this.languages = languages;
        notifyDataSetChanged();
    }

    @Override
    public DrawerLanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_navigation_item, parent, false);
        return new DrawerLanguageViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(DrawerLanguageViewHolder holder, int position) {
        ILanguage language = languages.get(position);
        holder.showLanguage(language);
    }

    @Override
    public int getItemCount() {
        return languages.size();
    }
}
