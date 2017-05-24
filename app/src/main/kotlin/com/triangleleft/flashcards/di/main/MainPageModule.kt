package com.triangleleft.flashcards.di.main

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Named

@Module
class MainPageModule {

    private val wordSelections = BehaviorSubject.create<VocabularyWord>()

    @Provides
    @Named(WORD_SELECTIONS)
    fun wordObserver(): Observer<VocabularyWord> {
        return wordSelections
    }

    @Provides
    @Named(WORD_SELECTIONS)
    fun wordSelections(): Observable<VocabularyWord> {
        return wordSelections
    }

    companion object {
        const val WORD_SELECTIONS = "wordSelections"
    }

}
