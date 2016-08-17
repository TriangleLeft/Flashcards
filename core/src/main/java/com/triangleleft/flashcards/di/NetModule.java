package com.triangleleft.flashcards.di;

import com.google.gson.Gson;

import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.util.NetworkDelayInterceptor;
import com.triangleleft.flashcards.util.converter.CustomGsonConverterFactory;

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
public class NetModule {
    private final static String BASE_SCHEME = "https";
    private final static String BASE_URL = "www.duolingo.com";

    @ApplicationScope
    @Provides
    public HttpUrl endpoint() {
        return new HttpUrl.Builder()
            .host(BASE_URL)
            .scheme(BASE_SCHEME)
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
                .sslSocketFactory()
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

    @Provides
    public Gson gson() {
        return new Gson();
    }
}
