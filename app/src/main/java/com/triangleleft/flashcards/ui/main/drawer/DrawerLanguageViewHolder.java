package com.triangleleft.flashcards.ui.main.drawer;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.ui.common.FlagImagesProvider;
import com.triangleleft.flashcards.ui.common.OnItemClickListener;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
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
        int typeface = isSelected ? Typeface.BOLD : Typeface.NORMAL;
        int color = isSelected ? R.color.colorPrimaryDark : R.color.textColorSecondary;

        nameView.setTypeface(null, typeface);
        badgeView.setTypeface(null, typeface);
        nameView.setTextColor(ContextCompat.getColor(itemView.getContext(), color));
        badgeView.setTextColor(ContextCompat.getColor(itemView.getContext(), color));

        itemView.invalidate();
    }

}
