package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.rest.model.LoginResponseModel;
import com.triangleleft.flashcards.service.rest.model.VocabularResponseModel;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by lekz112 on 08.10.2015.
 */
public interface IDuolingoRest {
    @FormUrlEncoded
    @POST("/login")
    Call<LoginResponseModel> login(@Field("login") String login, @Field("password") String password);

    @GET("/vocabulary/overview")
    Observable<VocabularResponseModel> getVocabularList(@Query("_") long timestamp);

}
