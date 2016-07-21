package com.triangleleft.flashcards.service.vocabular.rest.model;

import static com.annimon.stream.Collectors.toList;

import com.annimon.stream.Stream;
import com.google.gson.annotations.SerializedName;
import com.triangleleft.flashcards.service.vocabular.VocabularyData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;

import java.util.Collections;
import java.util.List;

public class VocabularyResponseModel {

    @SerializedName("learning_language")
    String learningLanguage;
    @SerializedName("from_language")
    String fromLanguage;
    @SerializedName("vocab_overview")
    List<VocabularyResponseWordModel> wordList;

    public VocabularyData toVocabularyData() {
        List<VocabularyWord> list = Stream.of(wordList)
            .map(word -> word.toVocabularyWord(fromLanguage, learningLanguage))
                .collect(toList());
        return VocabularyData.create(list, fromLanguage, learningLanguage);
    }

    static class VocabularyResponseWordModel {
        @SerializedName("normalized_string")
        String normalizedString;
        @SerializedName("strength_bars")
        int strengthBars;
        @SerializedName("word_string")
        String wordString;
        @SerializedName("id")
        String id;
        @SerializedName("pos")
        String pos;
        @SerializedName("gender")
        String gender;

        public VocabularyWord toVocabularyWord(String uiLanguage, String learningLanguage) {
            return VocabularyWord.create(
                            wordString,
                            normalizedString,
                            pos,
                            gender,
                            strengthBars,
                            Collections.emptyList(),
                            uiLanguage,
                            learningLanguage
            );
        }

    }


}
