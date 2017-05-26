package com.triangleleft.flashcards.di.main

import com.triangleleft.flashcards.di.ApplicationComponent
import com.triangleleft.flashcards.di.scope.ActivityScope
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.main.FlashcardSettingsDialog
import com.triangleleft.flashcards.ui.main.MainActivity
import com.triangleleft.flashcards.ui.main.MainPresenter
import com.triangleleft.flashcards.ui.main.NavigationView
import com.triangleleft.flashcards.ui.main.drawer.DrawerPresenter
import dagger.Component
import io.reactivex.Observable
import io.reactivex.Observer
import javax.inject.Named

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(MainPageModule::class))
interface MainPageComponent : ApplicationComponent {

    fun mainPresenter(): MainPresenter

    fun drawerPresenter(): DrawerPresenter

    @Named(MainPageModule.WORD_SELECTIONS)
    fun wordObserver(): Observer<VocabularyWord>

    @Named(MainPageModule.WORD_SELECTIONS)
    fun wordSelections(): Observable<VocabularyWord>

    fun inject(mainView: MainActivity)

    fun inject(navigationView: NavigationView)

    fun inject(flashcardSettingsDialog: FlashcardSettingsDialog)
}
