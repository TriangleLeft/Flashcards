package com.triangleleft.flashcards.main.di;

import com.triangleleft.flashcards.common.di.ApplicationComponent;
import com.triangleleft.flashcards.main.MainActivity;
import com.triangleleft.flashcards.mvp.common.di.component.IComponent;
import com.triangleleft.flashcards.mvp.common.di.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.main.MainPageModule;
import com.triangleleft.flashcards.mvp.main.MainPresenter;
import com.triangleleft.flashcards.mvp.vocabular.VocabularyNavigator;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = MainPageModule.class)
public interface MainPageComponent extends ApplicationComponent, IComponent {

    MainPresenter mainPresenter();

    VocabularyNavigator vocabularNavigator();

    void inject(MainActivity mainView);
}
