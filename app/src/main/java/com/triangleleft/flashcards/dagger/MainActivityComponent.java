package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.dagger.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {


}
