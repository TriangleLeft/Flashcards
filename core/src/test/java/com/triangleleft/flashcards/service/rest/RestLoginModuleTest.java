package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.SimpleLoginRequest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import javax.inject.Inject;


@RunWith(JUnit4.class)
public class RestLoginModuleTest {

//    @Rule
//    public Timeout globalTimeout = Timeout.seconds(5); // 5 seconds max per method tested

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    static LoginModule loginModule;


    // We don't check for actual login/password here
    private ILoginRequest request = new SimpleLoginRequest("login", "password");


    @BeforeClass
    public static void setUpClass() throws IOException {
    }

    @Before
    public void before() throws IOException {
        System.out.println("setUp");
    }

//    @Test
//    public void loginWithValidLoginAndPassword() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        IListener<ILoginResult> listener = result -> {
//            assertTrue(result.isSuccess());
//            assertEquals(LoginStatus.LOGGED, result.getResult());
//            latch.countDown();
//        };
//
//        enqueueResponseModel(LoginResponseModelTest.buildSuccessModel("123"));
//
//        loginModule.doRequest(request, listener);
//
//        assertTrue(latch.await(5, TimeUnit.SECONDS));
//    }

//    @Test
//    public void loginWithInvalidLogin() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        IListener<ILoginResult> listener = result -> {
//            assertFalse(result.isSuccess());
//            assertEquals(ErrorType.LOGIN, result.getError().getType());
//            latch.countDown();
//        };
//
//        enqueueResponseModel(LoginResponseModelTest.buildLoginFailureModel());
//
//        loginModule.doRequest(request, listener);
//
//        assertTrue(latch.await(5, TimeUnit.SECONDS));
//    }
//
//    @Test
//    public void loginWithInvalidPassword() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        IListener<ILoginResult> listener = result -> {
//            assertFalse(result.isSuccess());
//            assertEquals(ErrorType.PASSWORD, result.getError().getType());
//            latch.countDown();
//        };
//
//        enqueueResponseModel(LoginResponseModelTest.buildPasswordFailureModel());
//
//        loginModule.doRequest(request, listener);
//
//        assertTrue(latch.await(5, TimeUnit.SECONDS));
//    }
//
//    @Test
//    public void loginWithUnknownFailure() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        IListener<ILoginResult> listener = result -> {
//            assertFalse(result.isSuccess());
//            assertEquals(ErrorType.INTERNAL, result.getError().getType());
//            latch.countDown();
//        };
//
//        enqueueResponseModel(LoginResponseModelTest.buildUnknownFailureModel());
//
//        loginModule.doRequest(request, listener);
//
//        assertTrue(latch.await(5, TimeUnit.SECONDS));
//    }
//
//    @Test
//    public void loginWithConversionError() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        IListener<ILoginResult> listener = result -> {
//            assertFalse(result.isSuccess());
//            assertEquals(ErrorType.CONVERSION, result.getError().getType());
//            latch.countDown();
//        };
//
//        MockResponse response = new MockResponse();
//        // Chances are, our rest server can send us plain html page
//        response.setBody("<html></html>");
//        webServer.enqueue(response);
//
//        loginModule.doRequest(request, listener);
//
//        assertTrue(latch.await(5, TimeUnit.SECONDS));
//    }
//
//    @Test
//    public void loginWithServerError() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        IListener<ILoginResult> listener = result -> {
//            assertFalse(result.isSuccess());
//            assertEquals(ErrorType.SERVER, result.getError().getType());
//            latch.countDown();
//        };
//
//        MockResponse response = new MockResponse();
//        // Chances are, our rest server can send us plain html page
//        response.setBody("<html></html>");
//        response.setResponseCode(401);
//        webServer.enqueue(response);
//
//        loginModule.doRequest(request, listener);
//
//        assertTrue(latch.await(5, TimeUnit.SECONDS));
//    }
//
//    @Test
//    public void loginWithNetworkError() throws InterruptedException, IOException {
//        CountDownLatch latch = new CountDownLatch(1);
//        IListener<ILoginResult> listener = result -> {
//            assertFalse(result.isSuccess());
//            assertEquals(ErrorType.NETWORK, result.getError().getType());
//            latch.countDown();
//        };
//
//        webServer.shutdown();
//
//        loginModule.doRequest(request, listener);
//
//        assertTrue(latch.await(5, TimeUnit.SECONDS));
//    }
//
//    @Test
//    public void failDuplicateRequest() {
//        IListener<ILoginResult> listener = result -> fail();
//
//        loginModule.doRequest(request, listener);
//        // There is no server request so we show have some time, till timeout kicks in
//
//        exception.expect(DuplicateRequestException.class);
//        loginModule.doRequest(request, listener);
//    }
//
//    @Test
//    public void failUnregisterUnknownListener() {
//        IListener<ILoginResult> listener = result -> fail();
//
//        exception.expect(UnknownRequestException.class);
//        loginModule.unregisterListener(request, listener);
//    }
//
//    @Test
//    public void failRegisterUnknownRequest() {
//        IListener<ILoginResult> listener = result -> fail();
//
//        exception.expect(UnknownRequestException.class);
//        loginModule.registerListener(request, listener);
//    }
//
//    @Test
//    public void reattachListenerAndWaitForRequest() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        // This listener is not supposed to be called
//        IListener<ILoginResult> listener = result -> fail();
//
//        loginModule.doRequest(request, listener);
//        loginModule.unregisterListener(request, listener);
//
//        listener = result -> latch.countDown();
//        loginModule.registerListener(request, listener);
//
//        // Simulate server answer
//        enqueueResponseModel(LoginResponseModelTest.buildLoginFailureModel());
//
//        assertTrue(latch.await(5, TimeUnit.SECONDS));
//    }
//
//    @Test
//    public void reattachListenerForCompletedRequest() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        // This listener is not supposed to be called
//        IListener<ILoginResult> listener = result -> fail();
//
//        loginModule.doRequest(request, listener);
//        loginModule.unregisterListener(request, listener);
//
//        // Simulate server answer
//        enqueueResponseModel(LoginResponseModelTest.buildLoginFailureModel());
//
//        // Process request result
//        Thread.sleep(100);
//
//        listener = result -> latch.countDown();
//        loginModule.registerListener(request, listener);
//
//
//        assertTrue(latch.await(5, TimeUnit.SECONDS));
//    }



}
