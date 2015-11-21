package com.triangleleft.flashcards.service;

import android.os.Parcelable;

public interface IVocabularWord extends Parcelable {
    String getWord();
    int getStrength();
}
