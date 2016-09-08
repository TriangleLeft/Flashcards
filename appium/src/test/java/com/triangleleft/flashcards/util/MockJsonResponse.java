package com.triangleleft.flashcards.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.HttpURLConnection;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MockJsonResponse {
    String value();

    int httpCode() default HttpURLConnection.HTTP_OK;
}
