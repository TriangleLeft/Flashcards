package com.triangleleft.flashcards;

import com.triangleleft.flashcards.android.FlashcardsApplication;
import com.triangleleft.flashcards.dagger.component.ApplicationComponent;
import com.triangleleft.flashcards.dagger.module.ApplicationModule;
import com.triangleleft.flashcards.dagger.DaggerApplicationComponent;
import com.triangleleft.flashcards.dagger.DaggerLoginActivityComponent;
import com.triangleleft.flashcards.dagger.component.LoginActivityComponent;
import com.triangleleft.flashcards.dagger.module.LoginActivityModule;
import com.triangleleft.flashcards.dagger.module.NetModule;
import com.triangleleft.flashcards.dagger.module.ServiceModule;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.rest.IDuolingoRest;
import com.triangleleft.flashcards.mvp.login.presenter.ILoginPresenter;

import org.mockito.Mockito;

import android.support.annotation.NonNull;

public class MockFlashcardsApplication extends FlashcardsApplication {

    private LoginActivityComponent mockLoginActivityComponent = Mockito.mock(LoginActivityComponent.class);

    @NonNull
    @Override
    protected ApplicationComponent buildComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .serviceModule(new ServiceModule() {
                    @Override
                    public ILoginModule loginModule(IDuolingoRest service) {
                        return Mockito.mock(ILoginModule.class);
                    }
                })
                .netModule(new NetModule())
                .build();
    }

    @Override
    public LoginActivityComponent buildLoginActivityComponent() {
        return DaggerLoginActivityComponent.builder()
                .applicationComponent(getComponent())
                .loginActivityModule(new LoginActivityModule() {
                    @Override
                    public ILoginPresenter loginPresenter(ILoginModule module) {
                        return Mockito.mock(ILoginPresenter.class);
                    }
                })
                .build();
    }
}
