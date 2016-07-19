package com.triangleleft.flashcards.service.vocabular;

import android.database.sqlite.SQLiteDatabase;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.service.vocabular.VocabularyWordModel;
import com.triangleleft.service.vocabular.VocabularyWordTranslationModel;

import java.util.List;
import javax.inject.Inject;

@FunctionsAreNonnullByDefault
public class DbVocabularyWordsRepository implements VocabularyWordsRepository {

    private final SQLiteDatabase database;

    @Inject
    public DbVocabularyWordsRepository(VocabularySQLiteOpenHelper helper) {
        database = helper.getWritableDatabase();
    }

    @Override
    public List<VocabularyWord> getWords(String uiLanguageId, String learningLanguageId) {
        return VocabularyWordDao.allInfo(database, uiLanguageId, learningLanguageId);
    }

    @Override
    public void putWords(List<VocabularyWord> words) {
        // TODO: do we want to clear cache in case for some reason we no longer have words?
        if (words.isEmpty()) {
            return;
        }
        // We assume that all words in the list belong to the same group
        String uiLanguageId = words.get(0).getUiLanguage();
        String learningLanguageId = words.get(0).getLearningLanguage();
        database.beginTransaction();
        try {
            database.execSQL(VocabularyWordModel.DELETE_WORDS, new String[]{uiLanguageId, learningLanguageId});
            for (VocabularyWord word : words) {
                long id = database.insert(VocabularyWordModel.TABLE_NAME, null, VocabularyWordDao.FACTORY.marshal()
                        .uiLanguage(word.getUiLanguage())
                        .learningLanguage(word.getLearningLanguage())
                        .word_string(word.getWord())
                        .normalized_string(word.getNormalizedWord())
                        .gender(word.getGender())
                        .pos(word.getPos())
                        .strength(word.getStrength())
                        .asContentValues());
                for (String translation : word.getTranslations()) {
                    database.insert(VocabularyWordTranslationModel.TABLE_NAME, null,
                            VocabularWordTranslationDao.FACTORY.marshal()
                                    .word_id(id)
                                    .translation(translation)
                                    .asContentValues());
                }
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
