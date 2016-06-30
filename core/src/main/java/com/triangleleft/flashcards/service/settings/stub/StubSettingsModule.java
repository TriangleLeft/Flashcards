package com.triangleleft.flashcards.service.settings.stub;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.triangleleft.flashcards.service.settings.ILanguage;
import com.triangleleft.flashcards.service.settings.ISettingsModule;
import com.triangleleft.flashcards.service.settings.SimpleUserData;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

@FunctionsAreNonnullByDefault
public class StubSettingsModule implements ISettingsModule {

    private final static int DELAY = 300;
    private List<StubLanguage> languages = Arrays.asList(
            StubLanguage.create("en", "English", 4, false, false),
            StubLanguage.create("es", "Spanish", 3, true, false),
            StubLanguage.create("de", "German", 2, true, true),
            StubLanguage.create("fr", "French", 5, true, false),
            StubLanguage.create("du", "Dutch", 1, true, false)
    );
    private String avatarUrl =
            "http://i2.wp.com/bato.to/forums/public/style_images/subway/profile/default_large.png";
    private String userName = "userName";
    private String uiLanguage = "en";
    private String learningLanguage = " de";

    @Inject
    public StubSettingsModule() {

    }

    @Nullable
    @Override
    public UserData getCurrentUserData() {
        return SimpleUserData.create(
                Collections.unmodifiableList(languages),
                avatarUrl,
                userName,
                learningLanguage,
                uiLanguage);
    }

    @Override
    public Observable<UserData> getUserData() {
        return Observable.just(getCurrentUserData()).delay(DELAY, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Void> switchLanguage(@NonNull ILanguage language) {
        languages = Stream.of(languages)
                .map(stub -> stub.withCurrentLearning(stub.getId().equals(language.getId())))
                .sortBy(ILanguage::getId)
                .collect(Collectors.toList());
        return Observable.just((Void) null).delay(DELAY, TimeUnit.MILLISECONDS);
    }
}
