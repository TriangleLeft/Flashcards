package com.triangleleft.flashcards.dagger.module;

import com.triangleleft.flashcards.dagger.scope.ApplicationScope;
import com.triangleleft.flashcards.service.rest.IDuolingoRest;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
    public Retrofit retrofit(HttpUrl url, OkHttpClient client) {
        return new Retrofit.Builder().baseUrl(url).client(client)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    @ApplicationScope
    @Provides
    public OkHttpClient client(CookieJar cookieJar) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().cookieJar(cookieJar).addInterceptor(interceptor).build();
    }

    @ApplicationScope
    @Provides
    public CookieJar cookieJar() {
        return new CookieJar() {
            public List<Cookie> cookieList;

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                this.cookieList = cookies;
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                if (cookieList != null) {
                    return cookieList;
                } else {
                    return new ArrayList<>();
                }
            }
        };
    }

    @ApplicationScope
    @Provides
    public IDuolingoRest duolingoRest(Retrofit retrofit) {
        return retrofit.create(IDuolingoRest.class);
    }

}
