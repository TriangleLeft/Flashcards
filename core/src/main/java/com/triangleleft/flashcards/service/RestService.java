package com.triangleleft.flashcards.service;

import com.triangleleft.flashcards.service.cards.rest.FlashcardResponseModel;
import com.triangleleft.flashcards.service.cards.rest.FlashcardResultsController;
import com.triangleleft.flashcards.service.login.rest.LoginRequestController;
import com.triangleleft.flashcards.service.login.rest.LoginResponseModel;
import com.triangleleft.flashcards.service.settings.rest.model.LanguageDataModel;
import com.triangleleft.flashcards.service.settings.rest.model.SwitchLanguageController;
import com.triangleleft.flashcards.service.settings.rest.model.UserDataModel;
import com.triangleleft.flashcards.service.vocabular.rest.model.VocabularyResponseModel;
import com.triangleleft.flashcards.service.vocabular.rest.model.WordTranslationModel;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


@FunctionsAreNonnullByDefault
public interface RestService {

    String BASE_SCHEME = "https";
    String BASE_URL = "www.duolingo.com";
    String PATH_LOGIN = "/login";
    String PATH_VOCABULARY = "/vocabulary/overview";
    String PATH_FLASHCARDS = "/api/1/flashcards";
    String PATH_SWITCH_LANGUAGE = "/switch_language";
    String PATH_USERDATA = "/api/1/users/show";
    String URL_TRANSLATION = "https://d2.duolingo.com/api/1/dictionary/hints";

    String QUERY_USERID = "id";
    String QUERY_TIMESTAMP = "_";
    String QUERY_FLASHCARDS_COUNT = "n";
    String QUERY_ALLOW_PARTIAL_DECK = "allow_partial_deck";
    String QUERY_TOKENS = "tokens";

    @POST(PATH_LOGIN)
    Observable<LoginResponseModel> login(@Body LoginRequestController controller);

    @GET(PATH_VOCABULARY)
    Observable<VocabularyResponseModel> getVocabularyList(@Query(QUERY_TIMESTAMP) long timestamp);

    @GET(PATH_FLASHCARDS)
    Observable<FlashcardResponseModel> getFlashcardData(@Query(QUERY_FLASHCARDS_COUNT) int count,
                                                        @Query(QUERY_ALLOW_PARTIAL_DECK) boolean allowPartialDeck,
                                                        @Query(QUERY_TIMESTAMP) long timestamp);

    @POST(PATH_FLASHCARDS)
    Observable<Void> postFlashcardResults(@Body FlashcardResultsController model);

    @POST(PATH_SWITCH_LANGUAGE)
    Observable<LanguageDataModel> switchLanguage(@Body SwitchLanguageController controller);

    @GET(PATH_USERDATA)
    Observable<UserDataModel> getUserData(@Query(QUERY_USERID) String userId);

    @GET(URL_TRANSLATION + "/{from}/{to}")
    Observable<WordTranslationModel> getTranslation(@Path("from") String languageIdFrom,
                                                    @Path("to") String languageIdTo,
                                                    @Query(QUERY_TOKENS) String tokens);
}
