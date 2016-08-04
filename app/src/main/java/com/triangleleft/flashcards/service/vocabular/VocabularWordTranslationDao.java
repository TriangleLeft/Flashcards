package com.triangleleft.flashcards.service.vocabular;

import com.google.auto.value.AutoValue;
import com.triangleleft.service.vocabular.VocabularyWordTranslationModel;

@AutoValue
public abstract class VocabularWordTranslationDao implements VocabularyWordTranslationModel {

    public static final Factory<VocabularWordTranslationDao> FACTORY =
            new Factory<>(AutoValue_VocabularWordTranslationDao::new);

}
