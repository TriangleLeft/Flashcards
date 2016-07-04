package com.triangleleft.flashcards.service.vocabular;

import com.google.auto.value.AutoValue;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import java.util.List;

@FunctionsAreNonnullByDefault
@AutoValue
public abstract class VocabularWord {

    public abstract String getWord();

    public abstract String getNormalizedWord();

    @Nullable
    public abstract String getPos();

    @Nullable
    public abstract String getGender();

    @IntRange(from = 0, to = 4)
    public abstract int getStrength();

    public abstract List<String> getTranslations();

    public abstract String getUiLanguage();

    public abstract String getLearningLanguage();

    public static VocabularWord create(
            String word,
            String normalizedWord,
            @Nullable String pos,
            @Nullable String gender,
            int strength,
            List<String> translations,
            String uiLanguage,
            String learningLanguage) {
        return new AutoValue_VocabularWord(
                word,
                normalizedWord,
                pos,
                gender,
                strength,
                translations,
                uiLanguage,
                learningLanguage);
    }

    public abstract VocabularWord withWord(String word);

    public abstract VocabularWord withTranslations(List<String> strings);
}
