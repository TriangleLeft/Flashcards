package com.triangleleft.flashcards.util;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ResourcesUtils {

    public static final String LANGUAGE_SPANISH = "userdata/language_spanish.json";
    public static final String LANGUAGE_FRENCH = "userdata/language_french.json";

    public static final String USERDATA_SPANISH = "userdata/userdata_spanish.json";
    public static final String USERDATA_FRENCH = "userdata/userdata_french.json";

    public static final String VOCABULARY_SPANISH = "vocabulary/spanish.json";
    public static final String VOCABULARY_SPANISH_TRANSLATION = "vocabulary/spanish_translation.json";

    public static final String LOGIN_WRONG_LOGIN = "login/wrong_login_response.json";
    public static final String LOGIN_WRONG_PASSWORD = "login/wrong_password_response.json";
    public static final String LOGIN_SUCCESS = "login/valid_response.json";


    public static <T> T getModel(String json, Class<T> clazz) {
        return new Gson().fromJson(getReader(json), clazz);
    }

    public static BufferedReader getReader(String filename) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return new BufferedReader(new InputStreamReader(classloader.getResourceAsStream(filename)));
    }

}
