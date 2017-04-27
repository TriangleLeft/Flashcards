package com.triangleleft.flashcards.di;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.triangleleft.flashcards.BuildConfig;
import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.NetworkDelayInterceptor;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.TranslationService;
import com.triangleleft.flashcards.service.converter.CustomGsonConverterFactory;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@Module
public class NetworkModule {


    @ApplicationScope
    @Provides
    public RestService restService(Retrofit.Builder retrofit) {
        return retrofit.baseUrl(HttpUrl.parse(BuildConfig.REST_SERVICE_URL)).build().create(RestService.class);
    }

    @ApplicationScope
    @Provides
    public TranslationService translationService(Retrofit.Builder retrofit) {
        return retrofit.baseUrl(HttpUrl.parse(BuildConfig.REST_SERVICE_URL)).build().create(TranslationService.class);
    }

    @Provides
    public Retrofit.Builder retrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(CustomGsonConverterFactory.create(gson));
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
