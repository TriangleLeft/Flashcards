package com.triangleleft.flashcards.mvp.vocabular;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
public interface VocabularyNavigator {

    void showWord(Optional<VocabularyWord> word);

}
