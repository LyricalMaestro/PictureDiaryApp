package com.lyricaloriginal.picturediaryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LyricalMaestro on 15/01/24.
 */
public class DiaryDbAccessor {

    private DiaryDbAccessor() {
    }

    public boolean insert(Context context, DiaryActivity.DiaryModel model) {
        long result = -1;
        DiaryDbOpenHelper helper = new DiaryDbOpenHelper(context);
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            ContentValues cv = new ContentValues();
            cv.put("TITLE", model.title);
            cv.put("DATE", format.format(model.date));
            cv.put("NOTE", model.note);
            result = db.insert("T_DIARY", "", cv);
            db.setTransactionSuccessful();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
                db = null;
            }
        }
        return 0 <= result;
    }
}
