package com.triangleleft.flashcards.di.cards;

import com.triangleleft.flashcards.di.ApplicationComponent;
import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.ui.cards.FlashcardsActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface CardsComponent extends ApplicationComponent {
    void inject(FlashcardsActivity cardsActivity);
}
