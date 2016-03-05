package com.triangleleft.flashcards.service;

import com.triangleleft.flashcards.service.provider.IListener;

import java.util.List;

public interface IVocabularModule {
    List<IVocabularWord> getData();
    void loadData();
    void registerListener(IListener listener);
    void unregisterListener(IListener listener);

}
