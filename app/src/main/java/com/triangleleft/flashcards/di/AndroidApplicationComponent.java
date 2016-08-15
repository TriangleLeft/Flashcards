package com.triangleleft.flashcards.di;

import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.ui.common.FlagImagesProvider;

import dagger.Component;
import rx.Scheduler;

@ApplicationScope
@Component(modules = {AndroidApplicationModule.class, ApplicationModule.class, RestServiceModule.class,
        RetrofitModule.class})
public interface AndroidApplicationComponent extends ApplicationComponent {

    Scheduler mainThreadScheduler();

    FlagImagesProvider flagImagesProvider();
}
