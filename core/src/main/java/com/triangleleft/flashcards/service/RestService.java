package com.triangleleft.flashcards.service;

import com.triangleleft.flashcards.service.cards.rest.FlashcardResponseModel;
import com.triangleleft.flashcards.service.cards.rest.FlashcardResultsController;
import com.triangleleft.flashcards.service.login.rest.LoginRequestController;
import com.triangleleft.flashcards.service.login.rest.LoginResponseModel;
import com.triangleleft.flashcards.service.settings.rest.model.LanguageDataModel;
import com.triangleleft.flashcards.service.settings.rest.model.UserDataModel;
import com.triangleleft.flashcards.service.vocabular.rest.model.VocabularyResponseModel;
import com.triangleleft.flashcards.service.vocabular.rest.model.WordTranslationModel;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


@FunctionsAreNonnullByDefault
public interface RestService {

    String BASE_SCHEME = "https";
    String BASE_URL = "www.duolingo.com";
    String POST_LOGIN = "/login";
    String GET_VOCABULARY = "/vocabulary/overview";
    String GET_FLASHCARDS = "/api/1/flashcards";
    String POST_FLASHCARDS = "/api/1/flashcards";
    String SWITCH_LANGUAGE = "/switch_language";
    String GET_USERDATA = "/api/1/users/show";
    String GET_TRANSLATION = "https://d2.duolingo.com/api/1/dictionary/hints";

    @FormUrlEncoded
    @POST(POST_LOGIN)
    Observable<LoginResponseModel> login(@Body LoginRequestController model);

    @GET(GET_VOCABULARY)
    Observable<VocabularyResponseModel> getVocabularyList(@Query("_") long timestamp);

    @GET(GET_FLASHCARDS)
    Observable<FlashcardResponseModel> getFlashcardData(@Query("n") int count,
                                                        @Query("allow_partial_deck") boolean allowPartialDeck,
                                                        @Query("_") long timestamp);

    @POST(POST_FLASHCARDS)
    Observable<Void> postFlashcardResults(@Body FlashcardResultsController model);

    @FormUrlEncoded
    @POST(SWITCH_LANGUAGE)
    Observable<LanguageDataModel> switchLanguage(@Field("learning_language") String languageId);

    @GET(GET_USERDATA)
    Observable<UserDataModel> getUserData(@Query("id") String userId);

    @GET(GET_TRANSLATION + "/{from}/{to}")
    Observable<WordTranslationModel> getTranslation(@Path("from") String languageIdFrom,
                                                    @Path("to") String languageIdTo,
                                                    @Query("tokens") String tokens);
}
