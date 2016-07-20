package com.triangleleft.flashcards.ui.main.di;

import com.triangleleft.flashcards.ui.common.di.ApplicationComponent;
import com.triangleleft.flashcards.ui.common.di.component.IComponent;
import com.triangleleft.flashcards.ui.common.di.scope.ActivityScope;
import com.triangleleft.flashcards.ui.main.MainActivity;
import com.triangleleft.flashcards.ui.main.MainPageModule;
import com.triangleleft.flashcards.ui.main.MainPresenter;
import com.triangleleft.flashcards.ui.vocabular.VocabularyNavigator;
import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = MainPageModule.class)
public interface MainPageComponent extends ApplicationComponent, IComponent {

    MainPresenter mainPresenter();

    VocabularyNavigator vocabularNavigator();

    void inject(MainActivity mainView);
}
