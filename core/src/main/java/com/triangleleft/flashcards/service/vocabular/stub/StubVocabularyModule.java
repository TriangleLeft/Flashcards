package com.triangleleft.flashcards.service.vocabular.stub;

import com.annimon.stream.Stream;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.SimpleVocabularData;
import com.triangleleft.flashcards.service.vocabular.VocabularData;
import com.triangleleft.flashcards.service.vocabular.VocabularWordsCache;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

import static com.annimon.stream.Collectors.toList;

@FunctionsAreNonnullByDefault
public class StubVocabularyModule implements VocabularyModule {

    private final SettingsModule settingsModule;
    private final VocabularWordsCache provider;

    @Inject
    public StubVocabularyModule(SettingsModule settingsModule, VocabularWordsCache provider) {
        this.settingsModule = settingsModule;
        this.provider = provider;
    }

    @Override
    public Observable<List<VocabularyWord>> loadVocabularyWords() {
        Observable<List<VocabularyWord>> observable = refreshVocabularyWords();

        UserData userData = settingsModule.getCurrentUserData().get();
        observable.startWith(provider.getWords(userData.getUiLanguageId(), userData.getLearningLanguageId()));

        return observable;
    }

    @Override
    public Observable<List<VocabularyWord>> refreshVocabularyWords() {
        UserData userData = settingsModule.getCurrentUserData().get();

        return Observable.just(buildVocabularData(userData.getUiLanguageId(), userData.getLearningLanguageId()))
                        .subscribeOn(Schedulers.io())
                        .doOnNext(this::updateCache)
                        .map(VocabularData::getWords);
    }

    private void updateCache(VocabularData data) {
        List<VocabularyWord> words = Stream.of(data.getWords())
                .map(word -> word.withWord("cached_" + word.getWord()))
                .collect(toList());
        provider.putWords(words);
    }

    private VocabularData buildVocabularData(String uiLanguage, String learningLanguage) {
        List<VocabularyWord> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(VocabularyWord.create(
                    learningLanguage + "_word_" + i,
                    learningLanguage + "_word_" + i,
                    "pos",
                    "gender",
                    (int) (Math.random() * 4),
                    Collections.singletonList(uiLanguage + "_translation_" + i),
                    uiLanguage,
                    learningLanguage)
            );
        }
        return SimpleVocabularData.create(list, uiLanguage, learningLanguage);
    }
}
