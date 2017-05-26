package com.triangleleft.flashcards.ui

import com.squareup.leakcanary.RefWatcher
import com.triangleleft.flashcards.ui.common.ComponentManager

interface BaseApplication {
    val refWatcher: RefWatcher
    val componentManager: ComponentManager
}