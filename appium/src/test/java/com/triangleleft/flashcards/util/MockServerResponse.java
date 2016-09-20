package com.triangleleft.flashcards.util;


import java.net.HttpURLConnection;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.SocketPolicy;

public class MockServerResponse {

    public static MockResponse makeNetworkErrorResponse() {
        return new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START);
    }

    public static MockResponse make(MockJsonResponse response) {
        return new MockResponse().setBody(response.getBody()).setResponseCode(HttpURLConnection.HTTP_OK);
    }

//    public static MockResponse make(String fileName, int code) {
//        return new MockResponse().setBody(getJsonFromAsset(fileName)).setResponseCode(code);
//    }

}
