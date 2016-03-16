package com.triangleleft.flashcards.service.stub;

import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import android.os.Parcel;

public class StubVocabularWord implements IVocabularWord {

    private final String word;
    private final int strength;

    public StubVocabularWord(String word, int strength) {
        this.word = word;
        this.strength = strength;
    }
    @Override
    public String getWord() {
        return word;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.word);
        dest.writeInt(this.strength);
    }

    protected StubVocabularWord(Parcel in) {
        this.word = in.readString();
        this.strength = in.readInt();
    }

    public static final Creator<StubVocabularWord> CREATOR = new Creator<StubVocabularWord>() {
        public StubVocabularWord createFromParcel(Parcel source) {
            return new StubVocabularWord(source);
        }

        public StubVocabularWord[] newArray(int size) {
            return new StubVocabularWord[size];
        }
    };
}
