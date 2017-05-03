package com.triangleleft.flashcards.ui.main

import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import com.jakewharton.rxbinding2.view.RxView
import com.triangleleft.flashcards.R
import com.triangleleft.flashcards.service.settings.Language
import com.triangleleft.flashcards.ui.common.FlagImagesProvider
import io.reactivex.Observable

class DrawerLanguageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @Bind(R.id.drawer_item_icon)
    lateinit var iconView: ImageView
    @Bind(R.id.drawer_item_name)
    lateinit var nameView: TextView
    @Bind(R.id.drawer_item_description)
    lateinit var descriptionView: TextView
    @Bind(R.id.drawer_item_badge)
    lateinit var badgeView: TextView

    init {
        ButterKnife.bind(this, view)
    }


    fun showLanguage(flagImagesProvider: FlagImagesProvider, language: Language) {
        iconView.setImageDrawable(flagImagesProvider.getFlag(language.id))
        nameView.text = language.name
        descriptionView.visibility = View.GONE
        badgeView.text = itemView.resources.getString(R.string.language_level, language.level)

        val isSelected = language.isCurrentLearning
        val typeface = if (isSelected) Typeface.BOLD else Typeface.NORMAL
        val color = if (isSelected) R.color.colorPrimaryDark else R.color.textColorSecondary

        nameView.setTypeface(null, typeface)
        badgeView.setTypeface(null, typeface)
        nameView.setTextColor(ContextCompat.getColor(itemView.context, color))
        badgeView.setTextColor(ContextCompat.getColor(itemView.context, color))

        itemView.invalidate()
    }

    fun clicks(): Observable<DrawerLanguageViewHolder> {
        return RxView.clicks(itemView).map { this }
    }

}
