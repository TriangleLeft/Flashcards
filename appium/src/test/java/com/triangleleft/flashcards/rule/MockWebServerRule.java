package com.triangleleft.flashcards.rule;

import com.triangleleft.flashcards.util.MockJsonResponse;
import com.triangleleft.flashcards.util.MockServerResponse;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.net.InetAddress;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;

public class MockWebServerRule implements TestRule {

    public static final HttpUrl MOCK_SERVER_URL = HttpUrl.parse("http://localhost:8080/");

    private final MockWebServer webServer;

    public MockWebServerRule() {
        webServer = new MockWebServer();
    }

    @Override
    public Statement apply(Statement base, Description description) {
        try {
            webServer.start(InetAddress.getByName(MOCK_SERVER_URL.host()), MOCK_SERVER_URL.port());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // If method is annotated with our annotation, read json from resources and add it server responses
        MockJsonResponse annotation = description.getAnnotation(MockJsonResponse.class);
        if (annotation != null) {
            webServer.enqueue(MockServerResponse.make(annotation.value(), annotation.httpCode()));
        }
        return webServer.apply(base, description);
    }

    public MockWebServer getWebServer() {
        return webServer;
    }

}
