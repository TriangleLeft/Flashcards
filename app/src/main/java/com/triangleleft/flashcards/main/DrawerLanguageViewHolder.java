package com.triangleleft.flashcards.main;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.common.FlagImagesProvider;
import com.triangleleft.flashcards.common.OnItemClickListener;
import com.triangleleft.flashcards.service.settings.Language;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerLanguageViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.drawer_item_icon)
    ImageView iconView;
    @Bind(R.id.drawer_item_name)
    TextView nameView;
    @Bind(R.id.drawer_item_description)
    TextView descriptionView;
    @Bind(R.id.drawer_item_badge)
    TextView badgeView;

    public DrawerLanguageViewHolder(View view, OnItemClickListener<DrawerLanguageViewHolder> listener) {
        super(view);
        ButterKnife.bind(this, view);
        view.setOnClickListener(clickedView -> {
            listener.onItemClick(this, getAdapterPosition());
        });
    }


    public void showLanguage(FlagImagesProvider flagImagesProvider, Language language) {
        iconView.setImageDrawable(flagImagesProvider.getFlag(language.getId()));
        nameView.setText(language.getName());
        descriptionView.setVisibility(View.GONE);
        badgeView.setText(itemView.getResources().getString(R.string.language_level, language.getLevel()));

        boolean isSelected = language.isCurrentLearning();
        nameView.setSelected(isSelected);
        badgeView.setSelected(isSelected);
        if (isSelected) {
            nameView.setTypeface(null, Typeface.BOLD);
            badgeView.setTypeface(null, Typeface.BOLD);
        } else {
            nameView.setTypeface(null, Typeface.NORMAL);
            badgeView.setTypeface(null, Typeface.NORMAL);
        }


        itemView.invalidate();
    }
}
