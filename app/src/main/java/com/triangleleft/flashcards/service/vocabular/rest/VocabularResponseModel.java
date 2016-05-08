package com.triangleleft.flashcards.service.vocabular.rest;

import com.google.gson.annotations.SerializedName;

import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import android.os.Parcel;

import java.util.Collections;
import java.util.List;

public class VocabularResponseModel {

    @SerializedName("language_string")
    public String languageString;
    @SerializedName("learning_language")
    public String learningLanguage;
    @SerializedName("from_language")
    public String fromLanguage;
    @SerializedName("vocab_overview")
    public List<VocabularWordModel> wordList;

    public List<IVocabularWord> getWords() {
        return Collections.unmodifiableList(wordList);
    }

    public static class VocabularWordModel implements IVocabularWord {
        @SerializedName("normalized_string")
        public String normalizedString;
        @SerializedName("strength_bars")
        public int strengthBars;
        @SerializedName("word_string")
        public String wordString;

        @Override
        public String getWord() {
            return wordString;
        }

        @Override
        public int getStrength() {
            return strengthBars;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.normalizedString);
            dest.writeInt(this.strengthBars);
            dest.writeString(this.wordString);
        }

        public VocabularWordModel() {
        }

        protected VocabularWordModel(Parcel in) {
            this.normalizedString = in.readString();
            this.strengthBars = in.readInt();
            this.wordString = in.readString();
        }

        public static final Creator<VocabularWordModel> CREATOR = new Creator<VocabularWordModel>() {
            public VocabularWordModel createFromParcel(Parcel source) {
                return new VocabularWordModel(source);
            }

            public VocabularWordModel[] newArray(int size) {
                return new VocabularWordModel[size];
            }
        };

        @Override
        public String toString() {
            return getClass().getSimpleName() + "@" + normalizedString;
        }
    }


}
