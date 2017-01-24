package com.triangleleft.flashcards.service.vocabular;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.service.vocabular.VocabularyWordModel;
import com.triangleleft.service.vocabular.VocabularyWordTranslationModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import javax.inject.Inject;

@FunctionsAreNonnullByDefault
public class DbVocabularyWordsRepository implements VocabularyWordsRepository {

    private static final Logger logger = LoggerFactory.getLogger(DbVocabularyWordsRepository.class);
    private final VocabularyWordModel.Delete_words deleteWords;
    private final VocabularyWordModel.Insert_word insertWord;
    private final VocabularyWordTranslationModel.Insert_translation insertTranslation;
    private final SQLiteDatabase database;

    @Inject
    public DbVocabularyWordsRepository(VocabularySQLiteOpenHelper helper) {
        database = helper.getWritableDatabase();
        deleteWords = new VocabularyWordModel.Delete_words(database);
        insertWord = new VocabularyWordModel.Insert_word(database);
        insertTranslation = new VocabularyWordTranslationModel.Insert_translation(database);
    }

    @Override
    public List<VocabularyWord> getWords(String uiLanguageId, String learningLanguageId) {
        return VocabularyWordDao.allInfo(database, uiLanguageId, learningLanguageId);
    }

    @Override
    public void putWords(List<VocabularyWord> words) {
        logger.debug("putWords() called with list of size = [{}]", words.size());
        // TODO: do we want to clear cache in case for some reason we no longer have words?
        if (words.isEmpty()) {
            return;
        }
        // We assume that all words in the list belong to the same group
        String uiLanguageId = words.get(0).getUiLanguage();
        String learningLanguageId = words.get(0).getLearningLanguage();
        database.beginTransaction();
        try {
            deleteWords.bind(uiLanguageId, learningLanguageId);
            for (VocabularyWord word : words) {
                insertWord.bind(
                        word.getWord(),
                        word.getNormalizedWord(),
                        word.getPos().orElse(null),
                        word.getGender().orElse(null),
                        word.getStrength(),
                        word.getUiLanguage(),
                        word.getLearningLanguage()
                );
                long id = insertWord.program.executeInsert();
                for (String translation : word.getTranslations()) {
                    insertTranslation.bind(id, translation);
                    insertTranslation.program.executeInsert();
                }
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
