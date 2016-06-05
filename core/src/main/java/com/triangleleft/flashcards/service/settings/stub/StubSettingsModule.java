package com.triangleleft.flashcards.service.settings.stub;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.triangleleft.flashcards.service.settings.ILanguage;
import com.triangleleft.flashcards.service.settings.ISettingsModule;
import com.triangleleft.flashcards.service.settings.IUserData;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

@FunctionsAreNonnullByDefault
public class StubSettingsModule implements ISettingsModule {

    private final static int DELAY = 300;
    private List<ILanguage> languages = Arrays.asList(
            StubLanguage.create("en", "English", 4, false, false),
            StubLanguage.create("es", "Spanish", 3, true, false),
            StubLanguage.create("de", "German", 2, true, true),
            StubLanguage.create("fr", "French", 5, true, false),
            StubLanguage.create("du", "Dutch", 1, true, false)
    );
    private String avatarUrl =
            "http://i2.wp.com/bato.to/forums/public/style_images/subway/profile/default_large.png";
    private String userName = "userName";

    @Inject
    public StubSettingsModule() {

    }

    @Override
    public Observable<IUserData> getUserData() {
        IUserData userData = StubUserData.create(languages, avatarUrl, userName);
        return Observable.just(userData).delay(DELAY, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Void> switchLanguage(ILanguage language) {
        languages = Stream.of(languages)
                .map(stub -> StubLanguage.create(
                        stub.getId(),
                        stub.getName(),
                        stub.getLevel(),
                        stub.isLearning(),
                        stub.getId().equals(language.getId())))
                .sortBy(ILanguage::getId)
                .collect(Collectors.toList());
        return Observable.just((Void) null).delay(DELAY, TimeUnit.MILLISECONDS);
    }
}
