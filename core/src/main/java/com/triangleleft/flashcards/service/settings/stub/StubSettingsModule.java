package com.triangleleft.flashcards.service.settings.stub;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import rx.Observable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

@FunctionsAreNonnullByDefault
public class StubSettingsModule implements SettingsModule {

    private final static int DELAY = 300;
    private List<Language> languages = Arrays.asList(
        Language.create("en", "English", 4, false, false),
        Language.create("es", "Spanish", 3, true, false),
        Language.create("de", "German", 2, true, true),
        Language.create("fr", "French", 5, true, false),
        Language.create("du", "Dutch", 1, true, false)
    );
    private String avatarUrl =
        "http://i2.wp.com/bato.to/forums/public/style_images/subway/profile/default_large.png";
    private String userName = "userName";
    private String uiLanguage = "en";
    private String learningLanguage = " de";

    @Inject
    public StubSettingsModule() {

    }

    private Optional<UserData> getCurrentUserData() {
        return Optional.of(UserData.create(
            Collections.unmodifiableList(languages),
            avatarUrl,
            userName,
            uiLanguage,
            learningLanguage));
    }

    @Override
    public Observable<UserData> loadUserData() {
        return Observable.just(getCurrentUserData().get())
            .delay(DELAY, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Void> switchLanguage(Language language) {
        languages = Stream.of(languages)
            .map(stub -> stub.withCurrentLearning(stub.getId().equals(language.getId())))
            .sortBy(Language::getId)
            .collect(Collectors.toList());
        return Observable.just((Void) null).delay(DELAY, TimeUnit.MILLISECONDS);
    }
}
