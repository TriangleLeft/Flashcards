package com.triangleleft.flashcards.service.vocabular;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.triangleleft.service.vocabular.VocabularWordModel;
import com.triangleleft.service.vocabular.VocabularWordTranslationModel;

import javax.inject.Inject;

public class VocabularSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "vocabular.db";

    @Inject
    public VocabularSQLiteOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VocabularWordModel.CREATE_TABLE);
        db.execSQL(VocabularWordTranslationModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
