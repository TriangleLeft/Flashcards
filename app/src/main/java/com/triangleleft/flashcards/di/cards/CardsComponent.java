package com.triangleleft.flashcards.di.cards;

import com.triangleleft.flashcards.di.AndroidApplicationComponent;
import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.ui.cards.FlashcardsActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AndroidApplicationComponent.class)
public interface CardsComponent extends AndroidApplicationComponent {
    void inject(FlashcardsActivity cardsActivity);
}
