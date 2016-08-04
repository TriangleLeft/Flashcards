package com.triangleleft.flashcards.ui.cards.di;

import com.triangleleft.flashcards.ui.cards.FlashcardsActivity;
import com.triangleleft.flashcards.ui.common.di.ApplicationComponent;
import com.triangleleft.flashcards.ui.common.di.component.IComponent;
import com.triangleleft.flashcards.ui.common.di.scope.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface CardsComponent extends ApplicationComponent, IComponent {
    void inject(FlashcardsActivity cardsActivity);
}
