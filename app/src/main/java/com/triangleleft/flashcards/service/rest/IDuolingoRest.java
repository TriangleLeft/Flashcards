package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.rest.model.LoginResponseModel;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by lekz112 on 08.10.2015.
 */
public interface IDuolingoRest {
    @FormUrlEncoded
    @POST("/login")
    Call<LoginResponseModel> login(@Field("login") String login, @Field("password") String password);

}
