package com.triangleleft.flashcards.service.stub;

import com.triangleleft.flashcards.service.IVocabularModule;
import com.triangleleft.flashcards.vocab.VocabularWord;

import java.util.ArrayList;
import java.util.List;

public class StubVocabularModule implements IVocabularModule {

    private final ArrayList<VocabularWord> list;

    public StubVocabularModule() {
        this.list = new ArrayList<>();
        VocabularWord word;
        for (int i = 0; i < 500; i++) {
            word = new VocabularWord();
            word.word = "Test" + i;
            list.add(word);
        }
    }
    @Override
    public List<VocabularWord> getWords() {
        return list;
    }
}
