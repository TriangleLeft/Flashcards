package com.triangleleft.flashcards.di;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.NetworkDelayInterceptor;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.converter.CustomGsonConverterFactory;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;

@Module
public class RetrofitModule {


    @ApplicationScope
    @Provides
    public HttpUrl endpoint() {
        return new HttpUrl.Builder()
                .host(RestService.BASE_URL)
                .scheme(RestService.BASE_SCHEME)
            .build();
    }

    @ApplicationScope
    @Provides
    public Retrofit retrofit(HttpUrl url, OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
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
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }

    @ApplicationScope
    @Provides
    public NetworkDelayInterceptor delayInterceptor() {
        return new NetworkDelayInterceptor(0, TimeUnit.SECONDS);
    }

    @ApplicationScope
    @Provides
    public RestService duolingoRest(Retrofit retrofit) {
        return retrofit.create(RestService.class);
    }

    @ApplicationScope
    @Provides
    public CookieJar cookieJar(Context context) {
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }

}
