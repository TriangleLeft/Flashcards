package com.triangleleft.flashcards.di;

import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.RestService;

import dagger.Subcomponent;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;

@ApplicationScope
@Subcomponent(modules = {NetModule.class})
public interface NetComponent {

    RestService duolingoRest();

    HttpUrl endpoint();

    Retrofit retrofit();
}
