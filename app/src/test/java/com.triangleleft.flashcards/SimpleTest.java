package com.triangleleft.flashcards;

import com.google.gson.Gson;

import com.triangleleft.flashcards.dagger.ApplicationComponent;
import com.triangleleft.flashcards.dagger.ApplicationModule;
import com.triangleleft.flashcards.dagger.DaggerApplicationComponent;
import com.triangleleft.flashcards.dagger.NetModule;
import com.triangleleft.flashcards.dagger.ServiceModule;
import com.triangleleft.flashcards.service.IListener;
import com.triangleleft.flashcards.service.ILoginModule;
import com.triangleleft.flashcards.service.ILoginRequest;
import com.triangleleft.flashcards.service.ILoginResult;
import com.triangleleft.flashcards.service.SimpleLoginRequest;
import com.triangleleft.flashcards.service.rest.model.LoginResponseModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import android.support.annotation.NonNull;
import android.test.suitebuilder.annotation.SmallTest;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import timber.log.Timber;

@RunWith(JUnit4.class)
@SmallTest
public class SimpleTest extends Assert {

    private static final String VALID_LOGIN = "login";
    private static final String VALID_PASSWORD = "password";

//    @Rule
//    public Timeout globalTimeout = Timeout.seconds(5); // 5 seconds max per method tested

    @Inject
    ILoginModule loginModule;

    private static MockWebServer webServer;
    private Gson gson = new Gson();

    @BeforeClass
    public static void setUpClass() throws IOException {
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                System.out.println(tag + ": " + message);
            }
        });

        webServer = new MockWebServer();
        webServer.start();
    }

    @Before
    public void setUp() throws IOException {
        ApplicationComponent component =
                DaggerApplicationComponent.builder().applicationModule(Mockito.mock(ApplicationModule.class))
                        .netModule(new NetModule() {
                            @Override
                            public HttpUrl endpoint() {
                                return webServer.url("/");
                            }
                        }).serviceModule(new ServiceModule()).build();
        loginModule = component.loginModule();

    }

    @Test
    public void loginWithValidLoginAndPassword() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ILoginRequest request = new SimpleLoginRequest(VALID_LOGIN, VALID_PASSWORD);
        IListener<ILoginResult> listener = new IListener<ILoginResult>() {
            @Override
            public void onResult(@NonNull ILoginResult result) {
                assertTrue(result.isSuccess());
                latch.countDown();
            }
        };
        LoginResponseModel model = new LoginResponseModel();
        model.response = "OK";
        model.userId = "1212121131";

        MockResponse validResponce = new MockResponse();
        validResponce.setBody(gson.toJson(model));
        webServer.enqueue(validResponce);

        loginModule.doRequest(request, listener);

        latch.await();
    }
}
