package com.triangleleft.flashcards.test;

import android.annotation.SuppressLint;
import android.support.test.InstrumentationRegistry;

import java.io.BufferedReader;
import java.io.IOException;
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


    @SuppressLint("NewApi") // backported by retrolambda
    private static String getJsonFromAsset(String name) {
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
