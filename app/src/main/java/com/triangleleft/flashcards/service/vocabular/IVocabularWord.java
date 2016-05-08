package com.triangleleft.flashcards.service.vocabular;

import android.os.Parcelable;

public interface IVocabularWord extends Parcelable {

    String getWord();
    int getStrength();
}
