package com.triangleleft.flashcards.dagger.component;

import com.triangleleft.flashcards.android.main.MainActivity;
import com.triangleleft.flashcards.dagger.module.MainActivityModule;
import com.triangleleft.flashcards.dagger.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.main.presenter.IMainPresenter;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent extends IComponent {

    IMainPresenter mainPresenter();

    void inject(MainActivity mainActivity);
}
