package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.android.login.LoginActivityTestCase;

//@ApplicationScope
//@Component(modules = {ApplicationModule.class, ServiceModule.class, NetModule.class})
public interface MockApplicationComponent extends ApplicationComponent  {
    void inject(LoginActivityTestCase loginActivityTestCase);

}
