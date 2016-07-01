package com.triangleleft.flashcards.service.vocabular;

import com.google.auto.value.AutoValue;

import com.triangleleft.service.vocabular.VocabularWordTranslationModel;

@AutoValue
public abstract class VocabularWordTranslationDao implements VocabularWordTranslationModel {

    public static final Factory<VocabularWordTranslationDao> FACTORY =
            new Factory<>(AutoValue_VocabularWordTranslationDao::new);

}
