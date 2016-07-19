package com.triangleleft.flashcards.mvp.common.di.component;

import com.triangleleft.flashcards.mvp.common.di.module.NetModule;
import com.triangleleft.flashcards.mvp.common.di.scope.ApplicationScope;
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
