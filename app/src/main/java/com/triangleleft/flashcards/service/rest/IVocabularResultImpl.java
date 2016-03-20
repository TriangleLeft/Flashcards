package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.provider.SimpleProviderResult;
import com.triangleleft.flashcards.service.vocabular.IVocabularResult;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import android.support.annotation.NonNull;

import java.util.List;

public class IVocabularResultImpl extends SimpleProviderResult<List<IVocabularWord>> implements IVocabularResult {
    public IVocabularResultImpl(
            @NonNull List<IVocabularWord> data) {
        super(data);
    }
}
