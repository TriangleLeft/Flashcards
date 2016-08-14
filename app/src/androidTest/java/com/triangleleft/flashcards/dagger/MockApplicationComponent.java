package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.di.ApplicationComponent;
import com.triangleleft.flashcards.ui.login.LoginActivityTestCase;

//@ApplicationScope
//@Component(modules = {ApplicationModule.class, RestServiceModule.class, NetModule.class})
public interface MockApplicationComponent extends ApplicationComponent {
    void inject(LoginActivityTestCase loginActivityTestCase);

}
