package com.triangleleft.flashcards.service.vocabular.rest;

import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.common.AbstractProvider;
import com.triangleleft.flashcards.service.settings.ISettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.VocabularData;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;
import com.triangleleft.flashcards.service.vocabular.VocabularWordsCache;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

@FunctionsAreNonnullByDefault
public class RestVocabularModule extends AbstractProvider implements IVocabularModule {

    private static final Logger logger = LoggerFactory.getLogger(RestVocabularModule.class);
    private final IDuolingoRest service;
    private final ISettingsModule settingsModule;
    private final VocabularWordsCache provider;

    @Inject
    public RestVocabularModule(IDuolingoRest service, ISettingsModule settingsModule, VocabularWordsCache provider) {
        this.service = service;
        this.settingsModule = settingsModule;
        this.provider = provider;
    }

    @Override
    public Observable<List<VocabularWord>> getVocabularWords(boolean refresh) {
        logger.debug("getVocabularList() called");
        Observable<List<VocabularWord>> observable = service.getVocabularList(System.currentTimeMillis())
                .map(VocabularResponseModel::toVocabularData)
                .doOnNext(this::updateCache)
                .map(VocabularData::getWords);
        // For fresh calls, try to return db cache
        if (!refresh) {
            observable = observable.startWith(getCachedData());
        }
        return observable;
    }

    private void updateCache(VocabularData data) {
        provider.putWords(data.getWords(), data.getUiLanguageId(), data.getLearningLanguageId());
    }

    @Nullable
    private List<VocabularWord> getCachedData() {
        UserData userData = settingsModule.getCurrentUserData();
        return provider.getWords(userData.getUiLanguageId(), userData.getLearningLanguageId());
    }

}
