package com.triangleleft.flashcards.di;

import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import com.triangleleft.flashcards.ui.FlashcardsNavigator;
import com.triangleleft.flashcards.util.PersistentStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import okhttp3.CookieJar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(JUnit4.class)
public class DaggerTest {

    @Mock
    FlashcardsNavigator navigator;
    @Mock
    PersistentStorage storage;
    @Mock
    CookieJar cookieJar;
    @Mock
    VocabularyWordsRepository repository;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void buildComponent() {
        ApplicationComponent component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(navigator))
                .persistenceModule(new PersistenceModule(storage, cookieJar, repository))
                .build();

        assertThat(component.navigator(), is(equalTo(navigator)));
        assertThat(component.persistentStorage(), is(equalTo(storage)));
    }
}
