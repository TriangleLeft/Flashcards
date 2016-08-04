package com.triangleleft.flashcards.service.vocabular;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.annimon.stream.Optional;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;
import com.triangleleft.service.vocabular.VocabularyWordModel;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class VocabularyWordDao implements VocabularyWordModel {

    public static final Factory<VocabularyWordDao> FACTORY = new Factory<>(AutoValue_VocabularyWordDao::new);

    private static final RowMapper<AllInfo> SELECT_WORDS_MAPPER =
        FACTORY.select_wordsMapper(AutoValue_VocabularyWordDao_AllInfo::new);

    public static List<VocabularyWord> allInfo(SQLiteDatabase db, String uiLanguage, String learningLanguage) {
        List<VocabularyWord> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(SELECT_WORDS, new String[] { uiLanguage, learningLanguage })) {
            while (cursor.moveToNext()) {
                AllInfo info = SELECT_WORDS_MAPPER.map(cursor);
                VocabularyWordDao word = info.a();
                List<String> translations = new ArrayList<>();
                translations.add(info.translation());
                // Loop over cursor, add translation while we have matching key
                while (cursor.moveToNext()) {
                    info = SELECT_WORDS_MAPPER.map(cursor);
                    if (word.normalized_string().equals(info.a().normalized_string())) {
                        translations.add(info.translation());
                    } else {
                        // Key changed, roll-back
                        cursor.moveToPrevious();
                        break;
                    }
                }
                result.add(VocabularyWord.create(
                    word.word_string(),
                    word.normalized_string(),
                    Optional.ofNullable(word.pos()),
                    Optional.ofNullable(word.gender()),
                    (int) word.strength(),
                    translations,
                    word.uiLanguage(),
                    word.learningLanguage())
                );
            }
        }
        // TODO: try to rewrite using streams: get list of AllInfo, then group it, and map results
        return result;
    }

    @AutoValue
    protected abstract static class AllInfo implements Select_wordsModel<VocabularyWordDao> {

    }

}
