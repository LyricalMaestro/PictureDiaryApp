package com.lyricaloriginal.picturediaryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 絵日記データを保管するためのDatabaseを開くためのクラスです。
 * Created by LyricalMaestro on 15/01/24.
 */
class DiaryDbOpenHelper extends SQLiteOpenHelper {

    DiaryDbOpenHelper(Context context) {
        super(context, "diary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE T_DIARY (TITLE nchar(40), DATE nchar(8), NOTE TEXT, PICTURE_FILE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
