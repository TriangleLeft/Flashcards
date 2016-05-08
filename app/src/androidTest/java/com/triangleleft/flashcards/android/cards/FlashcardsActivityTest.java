package com.triangleleft.flashcards.android.cards;

import com.triangleleft.flashcards.MockFlashcardsApplication;
import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;
import com.triangleleft.flashcards.service.cards.rest.FlashcardResponseModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class FlashcardsActivityTest {

    //    @ClassRule
//    public static DisableAnimationsRule animationsRule = new DisableAnimationsRule();
    IDuolingoRest service;
    @Mock
    Call<FlashcardResponseModel> flashcardCall;
    @Mock
    ArgumentCaptor<Callback<FlashcardResponseModel>> callbackCaptor;


    public ActivityTestRule<FlashcardsActivity> activityRule =
            new ActivityTestRule<>(FlashcardsActivity.class, true, false);

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        MockFlashcardsApplication applicationContext =
                (MockFlashcardsApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        service = applicationContext.getComponent().duolingoRest();
        when(service.getFlashcardData()).thenReturn(flashcardCall);
    }

    @Test
    public void testLaunch() {
        FlashcardsActivity flashcardsActivity = activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));

        verify(flashcardCall).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(flashcardCall, Response.success(new FlashcardResponseModel() {
            @Override
            public List<IFlashcardWord> getWords() {
                return Arrays.asList(new StubFlashcardWord("word1", "tran1", "id1"),
                        new StubFlashcardWord("word2", "tran2", "id2"));
            }
        }));
        assertThat(flashcardsActivity, is(notNullValue()));
    }

    private class StubFlashcardWord implements IFlashcardWord {

        private final String word;
        private final String translation;
        private final String id;

        public StubFlashcardWord(String word, String translation, String id) {
            this.word = word;
            this.translation = translation;
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getTranslation() {
            return translation;
        }

        @Override
        public String getWord() {
            return word;
        }
    }
}