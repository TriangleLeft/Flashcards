package com.triangleleft.flashcards.ui.vocabular.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.triangleleft.flashcards.R
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import io.reactivex.subjects.PublishSubject

class VocabularyAdapter(
        private val selectable: Boolean,
        private val wordSelections: PublishSubject<VocabularyListView.Event.WordSelect>)
    : RecyclerView.Adapter<VocabularyViewHolder>() {

    private val NO_POSITION = -1
    private var list = emptyList<VocabularyWord>()
    private var selectedPosition = NO_POSITION

    fun setData(list: List<VocabularyWord>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabularyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vocabular_word, parent, false)
        return VocabularyViewHolder(view)
    }

    override fun onBindViewHolder(holder: VocabularyViewHolder, position: Int) {
        val word = getItem(position)
        holder.show(word, selectable && position == selectedPosition)
        holder.clicks().subscribe {
            setSelectedPosition(position)
            wordSelections.onNext(VocabularyListView.Event.WordSelect(word, position))
        }
    }

    fun getItem(position: Int): VocabularyWord {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setSelectedPosition(position: Int) {
        if (selectedPosition == position) {
            return
        }
        val oldPosition = selectedPosition
        this.selectedPosition = position
        // Clear old selected position
        if (oldPosition != NO_POSITION) {
            notifyItemChanged(oldPosition)
        }
        // Update new position
        if (selectable && position != NO_POSITION) {
            notifyItemChanged(position)
        }
    }
}
