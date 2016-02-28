package com.triangleleft.flashcards.service.stub;

import com.triangleleft.flashcards.service.IListener;
import com.triangleleft.flashcards.service.IVocabularModule;
import com.triangleleft.flashcards.service.IVocabularWord;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Stub realiztion of {@link IVocabularModule}.
 *
 * Calling {@link IVocabularModule#getData()} would always returns stub data.
 * Calling {@link IVocabularModule#loadData()} would immediately trigger {@link
 * IListener#onSuccess()} if there is any listener.
 */
public class StubVocabularModule implements IVocabularModule {

    private final ArrayList<IVocabularWord> list;
    private IListener listener;

    public StubVocabularModule() {
        this.list = new ArrayList<>();
        IVocabularWord word;
        Random random = new Random();
        for (int i = 0; i < 500; i++) {
            word = new StubVocabularWord("Test " + i, random.nextInt(4));
            list.add(word);
        }
    }


    @Override
    public List<IVocabularWord> getData() {
        return list;
    }

    @Override
    public void loadData() {
        if (listener != null) {
            listener.onSuccess();
        }
    }

    @Override
    public void registerListener(IListener listener) {
        this.listener = listener;
    }

    @Override
    public void unregisterListener(IListener listener) {
        if (this.listener == listener) {
            this.listener = null;
        }
    }
}
