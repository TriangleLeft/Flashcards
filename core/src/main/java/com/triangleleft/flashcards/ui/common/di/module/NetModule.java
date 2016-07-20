package com.triangleleft.flashcards.ui.common.di.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.ui.common.di.scope.ApplicationScope;
import com.triangleleft.flashcards.util.NetworkDelayInterceptor;
import com.triangleleft.flashcards.util.converter.CustomGsonConverterFactory;
import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

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
    public Retrofit retrofit(HttpUrl url, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(CustomGsonConverterFactory.create()).build();
    }

    @ApplicationScope
    @Provides
    public OkHttpClient client(CookieJar cookieJar, HttpLoggingInterceptor loggingInterceptor,
                               NetworkDelayInterceptor delayInterceptor) {
        return new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(delayInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
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

}
