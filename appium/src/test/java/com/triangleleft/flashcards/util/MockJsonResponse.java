package com.triangleleft.flashcards.util;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public enum MockJsonResponse {

    LANGUAGE_SPANISH("userdata/language_spanish.json"),
    LANGUAGE_FRENCH("userdata/language_french.json"),

    USERDATA_SPANISH("userdata/userdata_spanish.json"),
    USERDATA_FRENCH("userdata/userdata_french.json"),

    VOCABULARY_EMPTY("vocabulary/empty.json"),
    VOCABULARY_SPANISH("vocabulary/spanish.json"),
    VOCABULARY_SPANISH_TRANSLATION("vocabulary/spanish_translation.json"),

    LOGIN_WRONG_LOGIN("login/wrong_login_response.json"),
    LOGIN_WRONG_PASSWORD("login/wrong_password_response.json"),
    LOGIN_SUCCESS("login/valid_response.json"),

    FLASHCARDS_SPANISH("flashcards/spanish.json"),
    FLASHCARDS_SPANISH_SINGLE("flashcards/spanish_single.json"),

    EMPTY("empty.json");

    private final String resource;

    MockJsonResponse(String resource) {
        this.resource = resource;
    }

    public static <T> T getModel(MockJsonResponse response, Class<T> clazz) {
        return new Gson().fromJson(response.getBody(), clazz);
    }

    public static BufferedReader getReader(String filename) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return new BufferedReader(new InputStreamReader(classloader.getResourceAsStream(filename)));
    }

    public String getBody() {
        return getJsonFromAsset(resource);
    }

    private static String getJsonFromAsset(String filename) {
        try (BufferedReader reader = getReader(filename)) {

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
