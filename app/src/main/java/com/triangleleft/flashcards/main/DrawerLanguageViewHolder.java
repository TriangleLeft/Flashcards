package com.triangleleft.flashcards.main;

import com.triangleleft.flashcards.OnItemClickListener;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.settings.ILanguage;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerLanguageViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.navigation_item_icon)
    ImageView iconView;
    @Bind(R.id.navigation_item_name)
    TextView nameView;
    @Bind(R.id.navigation_item_description)
    TextView descriptionView;
    @Bind(R.id.navigation_item_badge)
    TextView badgeView;

    public DrawerLanguageViewHolder(View view, OnItemClickListener<DrawerLanguageViewHolder> listener) {
        super(view);
        ButterKnife.bind(this, view);
        view.setOnClickListener(clickedView -> listener.onItemClick(this, getAdapterPosition()));
    }


    public void showLanguage(ILanguage language) {
        iconView.setImageDrawable(new ColorDrawable(Color.RED));
        nameView.setText(language.getName());
        descriptionView.setVisibility(View.GONE);
        badgeView.setText(language.getLevel() + " lvl");
        if (language.isCurrentLearning()) {
            itemView.setAlpha(1f);
        } else {
            itemView.setAlpha(0.5f);
        }
    }
}
