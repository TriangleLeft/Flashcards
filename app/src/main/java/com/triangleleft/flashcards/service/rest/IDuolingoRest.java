package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.rest.model.LoginResponseModel;
import com.triangleleft.flashcards.service.rest.model.VocabularResponseModel;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by lekz112 on 08.10.2015.
 */
public interface IDuolingoRest {
    @FormUrlEncoded
    @POST("/login")
    Call<LoginResponseModel> login(@Field("login") String login, @Field("password") String password);

    @GET("/vocabulary/overview")
    Observable<Response<VocabularResponseModel>> getVocabularList(@Query("_") long timestamp);

}
