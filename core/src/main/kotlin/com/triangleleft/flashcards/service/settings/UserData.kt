package com.triangleleft.flashcards.service.settings

data class UserData(val languages: List<Language>, val avatar: String, val username: String, val uiLanguageId: String,
                    val learningLanguageId: String) {

    // We assume that is only one language that we are currently learning
    // Sort it, so it is always top of the list
    val sortedLanguages: List<Language>
        get() = languages
                .filter({ it.isLearning })
                .sortedBy { language -> language.isCurrentLearning }

    val currentLearningLanguage: Language?
        get() {
            return if (languages.isNotEmpty()) sortedLanguages[0] else null
        }
}
