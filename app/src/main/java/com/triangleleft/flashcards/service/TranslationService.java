package com.triangleleft.flashcards.service;

import com.triangleleft.flashcards.service.vocabular.rest.model.WordTranslationModel;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@FunctionsAreNonnullByDefault
public interface TranslationService {

    String QUERY_TOKENS = "tokens";
    String PATH_TRANSLATION = "/api/1/dictionary/hints";

    @GET(PATH_TRANSLATION + "/{from}/{to}")
    Observable<WordTranslationModel> getTranslation(@Path("from") String languageIdFrom,
                                                    @Path("to") String languageIdTo,
                                                    @Query(QUERY_TOKENS) String tokens);
}