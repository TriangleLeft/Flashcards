package com.triangleleft.flashcards.service.vocabular

import android.support.annotation.IntRange
import com.annimon.stream.Optional


data class VocabularyWord(
        val word: String,
        val normalizedWord: String,
        val pos: Optional<String>,
        val gender: Optional<String>,
        @IntRange(from = 0, to = 4) val strength: Int,
        val translations: List<String>,
        val uiLanguage: String,
        val learningLanguage: String)