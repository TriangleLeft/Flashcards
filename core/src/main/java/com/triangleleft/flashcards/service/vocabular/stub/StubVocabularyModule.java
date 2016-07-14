package com.triangleleft.flashcards.service.vocabular.stub;

import com.annimon.stream.Stream;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.SimpleVocabularyData;
import com.triangleleft.flashcards.service.vocabular.VocabularyData;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsCache;
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

    private final AccountModule accountModule;
    private final VocabularyWordsCache provider;

    @Inject
    public StubVocabularyModule(AccountModule accountModule, VocabularyWordsCache provider) {
        this.accountModule = accountModule;
        this.provider = provider;
    }

    @Override
    public Observable<List<VocabularyWord>> loadVocabularyWords() {
        Observable<List<VocabularyWord>> observable = refreshVocabularyWords();

        UserData userData = accountModule.getUserData().get();
        observable.startWith(provider.getWords(userData.getUiLanguageId(), userData.getLearningLanguageId()));

        return observable;
    }

    @Override
    public Observable<List<VocabularyWord>> refreshVocabularyWords() {
        UserData userData = accountModule.getUserData().get();

        return Observable.just(buildVocabularData(userData.getUiLanguageId(), userData.getLearningLanguageId()))
                        .subscribeOn(Schedulers.io())
                        .doOnNext(this::updateCache)
                .map(VocabularyData::getWords);
    }

    private void updateCache(VocabularyData data) {
        List<VocabularyWord> words = Stream.of(data.getWords())
                .map(word -> word.withWord("cached_" + word.getWord()))
                .collect(toList());
        provider.putWords(words);
    }

    private VocabularyData buildVocabularData(String uiLanguage, String learningLanguage) {
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
        return SimpleVocabularyData.create(list, uiLanguage, learningLanguage);
    }
}
