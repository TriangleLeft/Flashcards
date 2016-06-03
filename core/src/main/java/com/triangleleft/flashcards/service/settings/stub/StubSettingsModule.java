package com.triangleleft.flashcards.service.settings.stub;

import com.triangleleft.flashcards.service.settings.ILanguage;
import com.triangleleft.flashcards.service.settings.ISettingsModule;
import com.triangleleft.flashcards.service.settings.IUserData;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

@FunctionsAreNonnullByDefault
public class StubSettingsModule implements ISettingsModule {

    private final static int DELAY = 300;
    private final List<ILanguage> languages = Collections.unmodifiableList(Arrays.asList(
            StubLanguage.create("en", "English", 4, false, false),
            StubLanguage.create("es", "Spanish", 3, true, false),
            StubLanguage.create("de", "German", 2, true, true)
    ));
    private final String avatarUrl =
            "http://i2.wp.com/bato.to/forums/public/style_images/subway/profile/default_large.png";
    private final IUserData userData = StubUserData.create(languages, avatarUrl, "UserName");

    @Inject
    public StubSettingsModule() {

    }

    @Override
    public Observable<IUserData> getUserData() {
        return Observable.just(userData).delay(DELAY, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Void> switchLanguage(ILanguage language) {
        return Observable.just((Void) null).delay(DELAY, TimeUnit.MILLISECONDS);
    }
}
