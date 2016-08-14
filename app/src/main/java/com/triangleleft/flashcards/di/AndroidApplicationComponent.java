package com.triangleleft.flashcards.di;

import com.triangleleft.flashcards.di.scope.AndroidApplicationScope;
import com.triangleleft.flashcards.ui.common.FlagImagesProvider;

import dagger.Component;
import rx.Scheduler;

@AndroidApplicationScope
@Component(dependencies = ApplicationComponent.class, modules = AndroidApplicationModule.class)
public interface AndroidApplicationComponent extends ApplicationComponent {

    Scheduler mainThreadScheduler();

    FlagImagesProvider flagImagesProvider();
}
