package com.triangleleft.flashcards.rule;

import com.triangleleft.flashcards.util.MockJsonResponse;
import com.triangleleft.flashcards.util.MockServerResponse;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class MockWebServerRule implements TestRule {

    //  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MockWebServerRule.class);
    public static final HttpUrl MOCK_SERVER_URL = HttpUrl.parse("http://localhost:8080/");

    private final MockWebServer webServer;
    private final UrlDispatcher dispatcher;

    private class UrlDispatcher extends Dispatcher {

        private Map<String, BlockingQueue<MockResponse>> responses = new HashMap<>();

        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
            String path = request.getPath();
            System.out.println("Got request for: " + path);
            for (String knownPath : responses.keySet()) {
                if (path.contains(knownPath)) {
                    return responses.get(knownPath).take();
                }
            }

            return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
        }

        public void enqueue(String path, MockResponse response) {
            System.out.println("Got answer for: " + path);
            BlockingQueue<MockResponse> responses = this.responses.get(path);
            if (responses == null) {
                responses = new LinkedBlockingQueue<>();
                this.responses.put(path, responses);
            }
            responses.add(response);
        }
    }

    public MockWebServerRule() {
        webServer = new MockWebServer();
        dispatcher = new UrlDispatcher();
        webServer.setDispatcher(dispatcher);
    }

    @Override
    public Statement apply(Statement base, Description description) {
        try {
            webServer.start(InetAddress.getByName(MOCK_SERVER_URL.host()), MOCK_SERVER_URL.port());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        // If method is annotated with our annotation, read json from resources and add it server responses
//        MockJsonResponse annotation = description.getAnnotation(MockJsonResponse.class);
//        if (annotation != null) {
//            webServer.enqueue(MockServerResponse.make(annotation.value(), annotation.httpCode()));
//        }
        return webServer.apply(base, description);
    }

    public MockWebServer getWebServer() {
        return webServer;
    }

    public void enqueue(String path, MockResponse response) {
        dispatcher.enqueue(path, response);
    }

    public void enqueue(String path, MockJsonResponse response) {
        dispatcher.enqueue(path, MockServerResponse.make(response));
    }

}
