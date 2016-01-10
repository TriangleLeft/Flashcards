package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.LoginActivityTestCase;
import com.triangleleft.flashcards.dagger.scope.ApplicationScope;

import dagger.Component;

@ApplicationScope
@Component(modules = {ApplicationModule.class,  NetModule.class})
public interface MockApplicationComponent extends ApplicationComponent  {
    void inject(LoginActivityTestCase loginActivityTestCase);

}
