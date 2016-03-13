package com.triangleleft.flashcards.dagger.component;

import com.triangleleft.flashcards.dagger.module.MainActivityModule;
import com.triangleleft.flashcards.dagger.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {


}
