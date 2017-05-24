package com.triangleleft.flashcards.ui.main.drawer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.triangleleft.flashcards.R
import com.triangleleft.flashcards.service.settings.Language
import com.triangleleft.flashcards.ui.common.FlagImagesProvider
import io.reactivex.subjects.Subject

class DrawerLanguagesAdapter(private val flagImagesProvider: FlagImagesProvider,
                             private val languageClicks: Subject<Language>) : RecyclerView.Adapter<DrawerLanguageViewHolder>() {
    private var languages = emptyList<Language>()

    fun setData(languages: List<Language>) {
        this.languages = languages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerLanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_navigation_item, parent, false)
        return DrawerLanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrawerLanguageViewHolder, position: Int) {
        val language = getItem(position)
        holder.showLanguage(flagImagesProvider, language)
        holder.clicks().subscribe {
            languageClicks.onNext(language)
        }
    }

    override fun getItemCount(): Int {
        return languages.size
    }

    fun getItem(position: Int): Language {
        return languages[position]
    }
}
