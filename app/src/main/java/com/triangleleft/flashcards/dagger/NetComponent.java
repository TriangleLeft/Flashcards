package com.triangleleft.flashcards.dagger;

import com.squareup.okhttp.HttpUrl;
import com.triangleleft.flashcards.dagger.scope.ApplcationScope;
import com.triangleleft.flashcards.service.rest.IDuolingoRest;

import dagger.Subcomponent;
import retrofit.Retrofit;

@ApplcationScope
@Subcomponent(modules = {NetModule.class})
public interface NetComponent {

    IDuolingoRest duolingoRest();

    HttpUrl endpoint();

    Retrofit retrofit();
}
