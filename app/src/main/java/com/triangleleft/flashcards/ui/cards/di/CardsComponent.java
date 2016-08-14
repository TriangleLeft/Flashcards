package com.triangleleft.flashcards.ui.cards.di;

import com.triangleleft.flashcards.di.ApplicationComponent;
import com.triangleleft.flashcards.di.IComponent;
import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.ui.cards.FlashcardsActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface CardsComponent extends ApplicationComponent, IComponent {
    void inject(FlashcardsActivity cardsActivity);
}
