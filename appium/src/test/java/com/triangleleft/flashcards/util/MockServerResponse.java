package com.triangleleft.flashcards.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.SocketPolicy;

public class MockServerResponse {

    public static MockResponse makeNetworkErrorResponse() {
        return new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START);
    }
    public static MockResponse make(String fileName) {
        return new MockResponse().setBody(getJsonFromAsset(fileName)).setResponseCode(HttpURLConnection.HTTP_OK);
    }

    public static MockResponse make(String fileName, int code) {
        return new MockResponse().setBody(getJsonFromAsset(fileName)).setResponseCode(code);
    }


    private static String getJsonFromAsset(String filename) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getResource(filename)))) {

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

    private static InputStream getResource(String filename) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResourceAsStream(filename);
    }
}
