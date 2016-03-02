package com.triangleleft.flashcards.service.rest;

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
import com.triangleleft.flashcards.service.error.ErrorType;
import com.triangleleft.flashcards.service.rest.model.LoginResponseModel;
import com.triangleleft.flashcards.service.rest.model.LoginResponseModelTest;

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
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import timber.log.Timber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
@SmallTest
public class RestLoginModuleTest {

//    @Rule
//    public Timeout globalTimeout = Timeout.seconds(5); // 5 seconds max per method tested

    @Inject
    static ILoginModule loginModule;

    private static MockWebServer webServer;
    private Gson gson = new Gson();
    private static ApplicationComponent component;

    @BeforeClass
    public static void setUpClass() throws IOException {
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                System.out.println(tag + ": " + message);
            }
        });


    }

    @Before
    public void before() throws IOException {
        System.out.println("setUp");
        component = DaggerApplicationComponent.builder().applicationModule(Mockito.mock(ApplicationModule.class))
                .netModule(new NetModule() {
                    @Override
                    public HttpUrl endpoint() {
                        return webServer.url("/");
                    }
                }).serviceModule(new ServiceModule()).build();

        webServer = new MockWebServer();
        loginModule = component.loginModule();
    }

    @Test
    public void loginWithValidLoginAndPassword() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ILoginRequest request = new SimpleLoginRequest("login", "password");
        IListener<ILoginResult> listener = result -> {
            assertTrue(result.isSuccess());
            latch.countDown();
        };

        enqueueResponseModel(LoginResponseModelTest.buildSuccessModel("123"));

        loginModule.doRequest(request, listener);

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void loginWithInvalidLogin() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ILoginRequest request = new SimpleLoginRequest("login", "password");
        IListener<ILoginResult> listener = result -> {
            assertFalse(result.isSuccess());
            assertEquals(ErrorType.LOGIN, result.getError().getType());
            latch.countDown();
        };

        enqueueResponseModel(LoginResponseModelTest.buildLoginFailureModel());

        loginModule.doRequest(request, listener);

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void loginWithInvalidPassword() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ILoginRequest request = new SimpleLoginRequest("login", "password");
        IListener<ILoginResult> listener = result -> {
            assertFalse(result.isSuccess());
            assertEquals(ErrorType.PASSWORD, result.getError().getType());
            latch.countDown();
        };

        enqueueResponseModel(LoginResponseModelTest.buildPasswordFailureModel());

        loginModule.doRequest(request, listener);

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void loginWithConversionError() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ILoginRequest request = new SimpleLoginRequest("login", "password");
        IListener<ILoginResult> listener = result -> {
            assertFalse(result.isSuccess());
            assertEquals(ErrorType.CONVERSION, result.getError().getType());
            latch.countDown();
        };

        MockResponse response = new MockResponse();
        // Chances are, our rest server can send us plain html page
        response.setBody("<html></html>");
        webServer.enqueue(response);

        loginModule.doRequest(request, listener);

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void loginWithServerError() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ILoginRequest request = new SimpleLoginRequest("login", "password");
        IListener<ILoginResult> listener = result -> {
            assertFalse(result.isSuccess());
            assertEquals(ErrorType.SERVER, result.getError().getType());
            latch.countDown();
        };

        MockResponse response = new MockResponse();
        // Chances are, our rest server can send us plain html page
        response.setBody("<html></html>");
        response.setResponseCode(401);
        webServer.enqueue(response);

        loginModule.doRequest(request, listener);

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void loginWithNetworkError() throws InterruptedException, IOException {
        CountDownLatch latch = new CountDownLatch(1);
        ILoginRequest request = new SimpleLoginRequest("login", "password");
        IListener<ILoginResult> listener = result -> {
            assertFalse(result.isSuccess());
            assertEquals(ErrorType.NETWORK, result.getError().getType());
            latch.countDown();
        };

        webServer.shutdown();

        loginModule.doRequest(request, listener);

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }


    private void enqueueResponseModel(@NonNull LoginResponseModel model) {
        MockResponse response = new MockResponse();
        response.setBody(gson.toJson(model));
        webServer.enqueue(response);
    }

}
