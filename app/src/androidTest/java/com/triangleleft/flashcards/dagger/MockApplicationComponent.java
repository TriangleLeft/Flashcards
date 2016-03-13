package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.android.login.LoginActivityTestCase;
import com.triangleleft.flashcards.dagger.component.ApplicationComponent;

//@ApplicationScope
//@Component(modules = {ApplicationModule.class, ServiceModule.class, NetModule.class})
public interface MockApplicationComponent extends ApplicationComponent {
    void inject(LoginActivityTestCase loginActivityTestCase);

}
