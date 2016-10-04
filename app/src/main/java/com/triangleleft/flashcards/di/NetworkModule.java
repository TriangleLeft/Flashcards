package com.triangleleft.flashcards.di;

import com.google.gson.Gson;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.triangleleft.flashcards.BuildConfig;
import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.NetworkDelayInterceptor;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.TranslationService;
import com.triangleleft.flashcards.service.adapter.FlashcardsCallAdapterFactory;
import com.triangleleft.flashcards.service.converter.CustomGsonConverterFactory;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@Module
public class NetworkModule {


    @ApplicationScope
    @Provides
    public RestService restService(OkHttpClient client, Gson gson) {
        return retrofit(HttpUrl.parse(BuildConfig.REST_SERVICE_URL), client, gson).create(RestService.class);
    }

    @ApplicationScope
    @Provides
    public TranslationService translationService(OkHttpClient client, Gson gson) {
        return retrofit(HttpUrl.parse(BuildConfig.TRANSLATION_SERVICE_URL), client, gson)
                .create(TranslationService.class);
    }


    private Retrofit retrofit(HttpUrl url, OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(new FlashcardsCallAdapterFactory())
                .addConverterFactory(CustomGsonConverterFactory.create(gson)).build();
    }

    @ApplicationScope
    @Provides
    public OkHttpClient client(CookieJar cookieJar, HttpLoggingInterceptor loggingInterceptor,
                               NetworkDelayInterceptor delayInterceptor) {
        return new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(delayInterceptor)
                //         .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    @ApplicationScope
    @Provides
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @ApplicationScope
    @Provides
    public NetworkDelayInterceptor delayInterceptor() {
        return new NetworkDelayInterceptor(0, TimeUnit.SECONDS);
    }

    @ApplicationScope
    @Provides
    public CookieJar cookieJar(Context context) {
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }

}
