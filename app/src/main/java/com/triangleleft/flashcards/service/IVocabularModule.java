package com.triangleleft.flashcards.service;

import java.util.List;

public interface IVocabularModule {
    List<IVocabularWord> getData();
    void loadData();
    void registerListener(ICommonListener listener);
    void unregisterListener(ICommonListener listener);

}
