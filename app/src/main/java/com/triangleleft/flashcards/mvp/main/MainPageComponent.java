package com.triangleleft.flashcards.mvp.main;

import com.triangleleft.flashcards.android.main.MainActivity;
import com.triangleleft.flashcards.mvp.common.di.component.ApplicationComponent;
import com.triangleleft.flashcards.mvp.common.di.component.IComponent;
import com.triangleleft.flashcards.mvp.common.di.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.vocabular.IVocabularNavigator;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = MainPageModule.class)
public interface MainPageComponent extends ApplicationComponent, IComponent {

    MainPresenter mainPresenter();

    IVocabularNavigator vocabularNavigator();

    void inject(MainActivity mainView);
}
