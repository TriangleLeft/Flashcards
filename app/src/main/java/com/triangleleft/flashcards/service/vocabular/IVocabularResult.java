package com.triangleleft.flashcards.service.vocabular;

import com.triangleleft.flashcards.service.ICacheable;
import com.triangleleft.flashcards.service.common.IProviderResult;

import java.util.List;

public interface IVocabularResult extends IProviderResult<List<IVocabularWord>>, ICacheable {
}
