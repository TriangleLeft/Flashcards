package com.triangleleft.flashcards.ui.view

import android.widget.TextView

fun TextView.setTextIfChanged(text: String?) {
    if (this.text.toString() != text) {
        this.text = text
    }
}