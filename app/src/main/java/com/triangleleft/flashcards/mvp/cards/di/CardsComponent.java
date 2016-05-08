package com.triangleleft.flashcards.mvp.cards.di;

import com.triangleleft.flashcards.android.cards.FlashcardsActivity;
import com.triangleleft.flashcards.mvp.common.di.component.ApplicationComponent;
import com.triangleleft.flashcards.mvp.common.di.component.IComponent;
import com.triangleleft.flashcards.mvp.common.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface CardsComponent extends ApplicationComponent, IComponent {
    void inject(FlashcardsActivity cardsActivity);
}
