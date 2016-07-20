package com.triangleleft.flashcards.ui.common.di.component;

import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.ui.common.di.module.NetModule;
import com.triangleleft.flashcards.ui.common.di.scope.ApplicationScope;
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
