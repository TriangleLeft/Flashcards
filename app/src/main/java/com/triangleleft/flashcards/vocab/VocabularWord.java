package com.triangleleft.flashcards.vocab;

import android.os.Parcel;
import android.os.Parcelable;

public class VocabularWord implements Parcelable {

    public String word;

    public VocabularWord() {

    }

    protected VocabularWord(Parcel in) {
        word = in.readString();
    }

    public static final Creator<VocabularWord> CREATOR = new Creator<VocabularWord>() {
        @Override
        public VocabularWord createFromParcel(Parcel in) {
            return new VocabularWord(in);
        }

        @Override
        public VocabularWord[] newArray(int size) {
            return new VocabularWord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
    }
}
