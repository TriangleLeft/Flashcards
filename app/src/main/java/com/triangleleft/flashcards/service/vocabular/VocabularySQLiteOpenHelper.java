package com.triangleleft.flashcards.service.vocabular;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.triangleleft.service.vocabular.VocabularyWordModel;
import com.triangleleft.service.vocabular.VocabularyWordTranslationModel;

import javax.inject.Inject;

public class VocabularySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "vocabulary.db";

    @Inject
    public VocabularySQLiteOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        // NOTE: seems like that's the only thing that work
        // TODO: check it whether prama works for API 15
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VocabularyWordModel.CREATE_TABLE);
        db.execSQL(VocabularyWordModel.CREATE_INDEX);
        db.execSQL(VocabularyWordTranslationModel.CREATE_TABLE);
        db.execSQL(VocabularyWordTranslationModel.CREATE_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
