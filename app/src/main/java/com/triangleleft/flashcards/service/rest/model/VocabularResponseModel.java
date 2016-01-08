package com.triangleleft.flashcards.service.rest.model;

import com.google.gson.annotations.SerializedName;

import com.triangleleft.flashcards.service.IVocabularWord;

import android.os.Parcel;

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

    public class VocabularWordModel implements IVocabularWord {
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

        }
    }



}
