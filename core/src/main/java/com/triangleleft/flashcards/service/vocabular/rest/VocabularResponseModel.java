package com.triangleleft.flashcards.service.vocabular.rest;

import com.google.gson.annotations.SerializedName;

import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

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
        public String toString() {
            return getClass().getSimpleName() + "@" + normalizedString;
        }
    }


}
