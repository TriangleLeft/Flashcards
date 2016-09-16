package com.triangleleft.flashcards.service.settings;

import com.google.auto.value.AutoValue;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.triangleleft.flashcards.util.AutoGson;

import java.util.Comparator;
import java.util.List;

import static com.annimon.stream.Collectors.toList;

@AutoGson
@AutoValue
public abstract class UserData {

    private static final Comparator<Language> languageComparator =
            (l1, l2) -> Boolean.valueOf(l2.isCurrentLearning()).compareTo(l1.isCurrentLearning());

    public static UserData create(
        List<Language> languages,
        String avatar,
        String username,
        String uiLanguage,
        String learningLanguage) {
        return new AutoValue_UserData(
            languages,
            avatar,
            username,
            uiLanguage,
            learningLanguage);
    }

    public abstract List<Language> getLanguages();

    public abstract String getAvatar();

    public abstract String getUsername();

    public abstract String getUiLanguageId();

    public abstract String getLearningLanguageId();

    public abstract UserData withLanguages(List<Language> languages);

    public abstract UserData withLearningLanguageId(String learningLanguageId);

    public List<Language> getSortedLanguages() {
        // We assume that is only one language that we are currently learning
        // Sort it, so it is always top of the list
        return Stream.of(getLanguages())
                .filter(Language::isLearning)
                .sorted(languageComparator)
                .collect(toList());
    }

    public Optional<Language> getCurrentLearningLanguage() {
        List<Language> languages = getSortedLanguages();
        return languages.size() > 0 ? Optional.of(languages.get(0)) : Optional.empty();
    }

}
