package com.triangleleft.flashcards.mvp.main.di;

import com.triangleleft.flashcards.android.main.MainActivity;
import com.triangleleft.flashcards.dagger.component.ApplicationComponent;
import com.triangleleft.flashcards.dagger.component.IComponent;
import com.triangleleft.flashcards.dagger.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.main.presenter.IMainPresenter;
import com.triangleleft.flashcards.mvp.vocabular.IVocabularNavigator;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = MainPageModule.class)
public interface MainPageComponent extends ApplicationComponent, IComponent {

    IMainPresenter mainPresenter();

    IVocabularNavigator vocabularNavigator();

    void inject(MainActivity mainView);
}
