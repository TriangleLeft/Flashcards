package com.triangleleft.flashcards.dagger.module;

import com.triangleleft.flashcards.dagger.scope.ApplicationScope;
import com.triangleleft.flashcards.service.rest.IDuolingoRest;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    private final static String BASE_SCHEME = "https";
    private final static String BASE_URL = "www.duolingo.com";

    @ApplicationScope
    @Provides
    public HttpUrl endpoint() {
        return new HttpUrl.Builder().host(BASE_URL).scheme(BASE_SCHEME).build();
    }

    @ApplicationScope
    @Provides
    public Retrofit retrofit(HttpUrl url) {
        return new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    @ApplicationScope
    @Provides
    public IDuolingoRest duolingoRest(Retrofit retrofit) {
        return retrofit.create(IDuolingoRest.class);
    }

}
