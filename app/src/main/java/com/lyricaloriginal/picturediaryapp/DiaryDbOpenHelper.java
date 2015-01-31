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
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE T_DIARY (");
        sb.append("ID INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("TITLE nchar(40),");
        sb.append("DATE nchar(8),");
        sb.append("NOTE TEXT,");
        sb.append("PICTURE_FILE TEXT);");

        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
