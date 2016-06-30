package com.triangleleft.flashcards.service.vocabular;

import com.google.auto.value.AutoValue;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import android.support.annotation.Nullable;

import java.util.List;

@FunctionsAreNonnullByDefault
@AutoValue
public abstract class VocabularWord {

    public abstract String getWord();

    abstract String getNormalizedWord();

    abstract String getPos();

    @Nullable
    abstract String getGender();

    public abstract int getStrength();

    public abstract List<String> getTranslations();


    public static VocabularWord create(String word, String normalizedWord, String pos, @Nullable String gender, int strength,
                                       List<String> translations) {
        return new AutoValue_VocabularWord(word, normalizedWord, pos, gender, strength, translations);
    }

    public abstract VocabularWord withWord(String word);
}
