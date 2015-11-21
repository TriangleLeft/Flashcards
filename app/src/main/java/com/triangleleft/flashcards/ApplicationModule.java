package com.triangleleft.flashcards;

import com.squareup.okhttp.HttpUrl;
import com.triangleleft.flashcards.service.IFlashcardsService;
import com.triangleleft.flashcards.service.ILoginModule;
import com.triangleleft.flashcards.service.IVocabularModule;
import com.triangleleft.flashcards.service.rest.IDuolingoRest;
import com.triangleleft.flashcards.service.rest.RestLoginModule;
import com.triangleleft.flashcards.service.stub.StubFlashcardsService;
import com.triangleleft.flashcards.service.stub.StubVocabularModule;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class ApplicationModule {
    private final FlashcardsApplication application;
    private final static String BASE_SCHEME = "https";
    private final static String BASE_URL = "www.duolingo.com";

    public ApplicationModule(FlashcardsApplication application) {
        this.application = application;
    }

    @Provides
    public FlashcardsApplication application() {
        return application;
    }

    @Singleton
    @Provides
    public IFlashcardsService service() {
        return new StubFlashcardsService();
    }

    @Singleton
    @Provides
    public ILoginModule loginModule(IDuolingoRest duolingoRest) {
        return new RestLoginModule(duolingoRest);
    }

    @Singleton
    @Provides
    public IDuolingoRest duolingoRest(Retrofit retrofit) {
        return retrofit.create(IDuolingoRest.class);
    }

    @Singleton
    @Provides
    public HttpUrl duolingoHttpUrl() {
        return new HttpUrl.Builder().host(BASE_URL).scheme(BASE_SCHEME).build();
    }

    @Singleton
    @Provides
    public Retrofit retrofit(HttpUrl url) {
        return new Retrofit.Builder().baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    @Singleton
    @Provides
    public IVocabularModule vocabularModule() {
        return new StubVocabularModule();
    }

    @Singleton
    @Provides
    public SharedPreferences preferences() {
        return application.getSharedPreferences(ApplicationModule.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
}
