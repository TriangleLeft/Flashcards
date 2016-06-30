package com.triangleleft.flashcards.service.vocabular;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.service.vocabular.VocabularWordModel;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import javax.inject.Inject;

@FunctionsAreNonnullByDefault
public class DbVocabularWordsCache implements VocabularWordsCache {

    private final SQLiteDatabase database;

    @Inject
    public DbVocabularWordsCache(VocabularSQLiteOpenHelper helper) {
        database = helper.getWritableDatabase();
    }

    @Override
    public List<VocabularWord> getWords(String uiLanguageId, String learningLanguageId) {
        return VocabularWordDao.allInfo(database, uiLanguageId, learningLanguageId);
    }

    @Override
    public void putWords(List<VocabularWord> words, String uiLanguageId, String learningLanguageId) {
        database.beginTransaction();
        try {
            database.execSQL(VocabularWordModel.DELETE_WORDS);
            for (VocabularWord word : words) {
                database.insert(VocabularWordModel.TABLE_NAME, null, VocabularWordDao.FACTORY.marshal()
                        .uiLanguage(uiLanguageId)
                        .learningLanguage(learningLanguageId)
                        .word_string(word.getWord())
                        .normalized_string(word.getNormalizedWord())
                        .gender(word.getGender())
                        .pos(word.getPos())
                        .strength(word.getStrength())
                        .asContentValues());
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
