package com.triangleleft.flashcards;

import com.triangleleft.flashcards.test.MockJsonResponse;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import android.annotation.SuppressLint;
import android.support.test.InstrumentationRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
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
        // If method is annotated with our annotation, read json from assets and add it server responses
        MockJsonResponse annotation = description.getAnnotation(MockJsonResponse.class);
        if (annotation != null) {
            webServer.enqueue(new MockResponse().setBody(getJsonFromAsset(annotation.value()))
                    .setResponseCode(annotation.errorCode()));
        }
        return webServer.apply(base, description);
    }

    public MockWebServer getWebServer() {
        return webServer;
    }

    @SuppressLint("NewApi")
    private String getJsonFromAsset(String name) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(InstrumentationRegistry.getContext().getAssets().open(name)))) {

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
