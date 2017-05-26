package com.triangleleft.flashcards.ui.vocabular.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import com.jakewharton.rxbinding2.view.RxView
import com.triangleleft.flashcards.R
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.vocabular.VocabularyStrengthView
import io.reactivex.Observable

class VocabularyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @Bind(R.id.item_vocabular_selected_mark)
    lateinit var selectedMark: View
    @Bind(R.id.item_vocabular_text)
    lateinit var textView: TextView
    @Bind(R.id.item_vocabular_strength)
    lateinit var strengthView: VocabularyStrengthView

    init {
        ButterKnife.bind(this, itemView)
    }

    fun show(word: VocabularyWord, isSelected: Boolean) {
        textView.text = word.word
        selectedMark.isSelected = isSelected
        strengthView.setStrength(word.strength)
    }

    fun clicks(): Observable<VocabularyViewHolder> {
        return RxView.clicks(itemView).map { this }
    }
}
