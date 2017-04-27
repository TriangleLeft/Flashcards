package com.triangleleft.flashcards.ui.vocabular

import com.triangleleft.flashcards.service.vocabular.VocabularyWord

data class VocabularyWordSelectEvent(val word: VocabularyWord, val position: Int)