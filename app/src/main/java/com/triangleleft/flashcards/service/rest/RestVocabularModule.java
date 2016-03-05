package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.provider.IListener;
import com.triangleleft.flashcards.service.IVocabularModule;
import com.triangleleft.flashcards.service.IVocabularWord;

import java.util.List;

import javax.inject.Inject;

public class RestVocabularModule implements IVocabularModule {
    private final IDuolingoRest service;
    private IListener listener;

    @Inject
    public RestVocabularModule(IDuolingoRest service) {
        this.service = service;
    }


    @Override
    public List<IVocabularWord> getData() {
        return null;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void registerListener(IListener listener) {

    }

    @Override
    public void unregisterListener(IListener listener) {

    }
}
