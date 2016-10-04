package com.triangleleft.flashcards.service.vocabular.stub;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyData;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.annimon.stream.Collectors.toList;

@FunctionsAreNonnullByDefault
public class StubVocabularyModule implements VocabularyModule {

    private final AccountModule accountModule;
    private final VocabularyWordsRepository provider;

    @Inject
    public StubVocabularyModule(AccountModule accountModule, VocabularyWordsRepository provider) {
        this.accountModule = accountModule;
        this.provider = provider;
    }

    @Override
    public Call<List<VocabularyWord>> loadVocabularyWords() {
        return refreshVocabularyWords();
    }

    @Override
    public Call<List<VocabularyWord>> refreshVocabularyWords() {
        UserData userData = accountModule.getUserData().get();

        return Call.just(buildVocabularyData(userData.getUiLanguageId(), userData.getLearningLanguageId()))
                .map(VocabularyData::getWords);
    }

    private void updateCache(VocabularyData data) {
        List<VocabularyWord> words = Stream.of(data.getWords())
            .map(word -> word.withWord("cached_" + word.getWord()))
            .collect(toList());
        provider.putWords(words);
    }

    private VocabularyData buildVocabularyData(String uiLanguage, String learningLanguage) {
        List<VocabularyWord> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(VocabularyWord.create(
                learningLanguage + "_word_" + i,
                learningLanguage + "_word_" + i,
                Optional.of("pos"),
                Optional.of("gender"),
                (int) (Math.random() * 4),
                Collections.singletonList(uiLanguage + "_translation_" + i),
                uiLanguage,
                learningLanguage)
            );
        }
        return VocabularyData.create(list, uiLanguage, learningLanguage);
    }

}
