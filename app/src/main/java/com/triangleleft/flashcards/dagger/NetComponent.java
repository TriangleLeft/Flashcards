package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.dagger.scope.ApplicationScope;
import com.triangleleft.flashcards.service.rest.IDuolingoRest;

import dagger.Subcomponent;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;

@ApplicationScope
@Subcomponent(modules = {NetModule.class})
public interface NetComponent {

    IDuolingoRest duolingoRest();

    HttpUrl endpoint();

    Retrofit retrofit();
}
