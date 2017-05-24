package com.triangleleft.flashcards.service.settings

data class Language(
        val id: String,
        val name: String,
        val level: Int,
        val isLearning: Boolean,
        val isCurrentLearning: Boolean)
