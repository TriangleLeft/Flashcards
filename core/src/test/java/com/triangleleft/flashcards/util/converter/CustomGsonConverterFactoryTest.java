///*
// *
// * =======================================================================
// *
// * Copyright (c) 2014-2015 Domlex Limited. All rights reserved.
// *
// * This software is the confidential and proprietary information of
// * Domlex Limited.
// * You shall not disclose such Confidential Information and shall use it only in
// * accordance with the terms of the license agreement you entered into with
// * Domlex Limited.
// *
// * =======================================================================
// *
// */
//
//package com.triangleleft.flashcards.util.converter;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.nullValue;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.core.IsEqual.equalTo;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.TypeAdapter;
//import com.google.gson.stream.JsonReader;
//import com.google.gson.stream.JsonToken;
//import com.google.gson.stream.JsonWriter;
//import com.triangleleft.flashcards.service.common.exception.ConversionException;
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//import okhttp3.mockwebserver.RecordedRequest;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//import org.mockito.runners.MockitoJUnitRunner;
//import retrofit2.Call;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.http.Body;
//import retrofit2.http.POST;
//
//import java.io.IOException;
//
///**
// * Taken from https://github.com/square/retrofit/blob/master/retrofit-converters/gson/src/test/java/retrofit2/converter/gson/GsonConverterFactoryTest.java
// * With one custom test: {@link #throwConversionException()}
// */
//@RunWith(JUnit4.class)
//public class CustomGsonConverterFactoryTest {
//
//    @Rule
//    public final MockWebServer server = new MockWebServer();
//    @Rule
//    public final ExpectedException expectedException = ExpectedException.none();
//
//    private Service service;
//
//    @Before
//    public void setUp() {
//        Gson gson = new GsonBuilder()
//            .registerTypeAdapter(AnInterface.class, new AnInterfaceAdapter())
//            .setLenient()
//            .create();
//        Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(server.url("/"))
//            .addConverterFactory(CustomGsonConverterFactory.create(gson))
//            .build();
//        service = retrofit.create(Service.class);
//    }
//
//    @Test
//    public void anInterface() throws IOException, InterruptedException {
//        server.enqueue(new MockResponse().setBody("{\"name\":\"value\"}"));
//
//        Call<AnInterface> call = service.anInterface(new AnImplementation("value"));
//        Response<AnInterface> response = call.execute();
//        AnInterface body = response.body();
//        assertThat(body.getName(), equalTo("value"));
//
//        RecordedRequest request = server.takeRequest();
//        assertThat(request.getBody().readUtf8(), equalTo("{\"name\":\"value\"}"));
//        assertThat(request.getHeader("Content-Type"), equalTo("application/json; charset=UTF-8"));
//    }
//
//    @Test
//    public void anImplementation() throws IOException, InterruptedException {
//        server.enqueue(new MockResponse().setBody("{\"theName\":\"value\"}"));
//
//        Call<AnImplementation> call = service.anImplementation(new AnImplementation("value"));
//        Response<AnImplementation> response = call.execute();
//        AnImplementation body = response.body();
//        assertThat(body.theName, equalTo("value"));
//
//        RecordedRequest request = server.takeRequest();
//        assertThat(request.getBody().readUtf8(), equalTo("{\"theName\":\"value\"}"));
//        assertThat(request.getHeader("Content-Type"), equalTo("application/json; charset=UTF-8"));
//    }
//
//    @Test
//    public void serializeUsesConfiguration() throws IOException, InterruptedException {
//        server.enqueue(new MockResponse().setBody("{}"));
//
//        service.anImplementation(new AnImplementation(null)).execute();
//
//        RecordedRequest request = server.takeRequest();
//        assertThat(request.getBody().readUtf8(), equalTo("{}")); // Null value was not serialized.
//        assertThat(request.getHeader("Content-Type"), equalTo("application/json; charset=UTF-8"));
//    }
//
//    @Test
//    public void deserializeUsesConfiguration() throws IOException, InterruptedException {
//        server.enqueue(new MockResponse().setBody("{/* a comment! */}"));
//
//        Response<AnImplementation> response =
//            service.anImplementation(new AnImplementation("value")).execute();
//        assertThat(response.body().getName(), is(nullValue()));
//    }
//
//    @Test
//    public void throwConversionException() throws InterruptedException, IOException {
//        server.enqueue(new MockResponse().setBody("<html></html>"));
//
//        expectedException.expect(ConversionException.class);
//        service.anImplementation(new AnImplementation("value")).execute();
//    }
//
//    @Test
//    public void failWithoutGson() {
//        expectedException.expect(NullPointerException.class);
//        GsonConverterFactory factory = GsonConverterFactory.create(null);
//    }
//
//    interface AnInterface {
//        String getName();
//    }
//
//    static class AnImplementation implements AnInterface {
//        private final String theName;
//
//        AnImplementation(String name) {
//            theName = name;
//        }
//
//        @Override
//        public String getName() {
//            return theName;
//        }
//    }
//
//    static class AnInterfaceAdapter extends TypeAdapter<AnInterface> {
//        @Override
//        public void write(JsonWriter jsonWriter, AnInterface anInterface) throws IOException {
//            jsonWriter.beginObject();
//            jsonWriter.name("name").value(anInterface.getName());
//            jsonWriter.endObject();
//        }
//
//        @Override
//        public AnInterface read(JsonReader jsonReader) throws IOException {
//            jsonReader.beginObject();
//
//            String name = null;
//            while (jsonReader.peek() != JsonToken.END_OBJECT) {
//                switch (jsonReader.nextName()) {
//                case "name":
//                    name = jsonReader.nextString();
//                    break;
//                }
//            }
//
//            jsonReader.endObject();
//            return new AnImplementation(name);
//        }
//    }
//
//    interface Service {
//        @POST("/")
//        Call<AnImplementation> anImplementation(@Body AnImplementation impl);
//
//        @POST("/")
//        Call<AnInterface> anInterface(@Body AnInterface impl);
//    }
//
//}