package com.triangleleft.flashcards.dagger;

import com.squareup.okhttp.HttpUrl;
import com.triangleleft.flashcards.dagger.scope.ApplcationScope;
import com.triangleleft.flashcards.service.rest.IDuolingoRest;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

@Module
public class NetModule {
    private final static String BASE_SCHEME = "https";
    private final static String BASE_URL = "www.duolingo.com";

    @ApplcationScope
    @Provides
    public HttpUrl endpoint() {
        return new HttpUrl.Builder().host(BASE_URL).scheme(BASE_SCHEME).build();
    }

    @ApplcationScope
    @Provides
    public Retrofit retrofit(HttpUrl url) {
        return new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    @ApplcationScope
    @Provides
    public IDuolingoRest duolingoRest(Retrofit retrofit) {
        return retrofit.create(IDuolingoRest.class);
    }

}
