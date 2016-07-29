package com.triangleleft.flashcards.service.vocabular;

import android.support.annotation.IntRange;

import com.annimon.stream.Optional;
import com.google.auto.value.AutoValue;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.List;

@FunctionsAreNonnullByDefault
@AutoValue
public abstract class VocabularyWord {

    public static VocabularyWord create(
        String word,
        String normalizedWord,
        Optional<String> pos,
        Optional<String> gender,
        int strength,
        List<String> translations,
        String uiLanguage,
        String learningLanguage) {
        return new AutoValue_VocabularyWord(
            word,
            normalizedWord,
            pos,
            gender,
            strength,
            translations,
            uiLanguage,
            learningLanguage);
    }

    public abstract String getWord();

    public abstract String getNormalizedWord();

    public abstract Optional<String> getPos();

    public abstract Optional<String> getGender();

    @IntRange(from = 0, to = 4)
    public abstract int getStrength();

    public abstract List<String> getTranslations();

    public abstract String getUiLanguage();

    public abstract String getLearningLanguage();

    public abstract VocabularyWord withWord(String word);

    public abstract VocabularyWord withTranslations(List<String> strings);
}
