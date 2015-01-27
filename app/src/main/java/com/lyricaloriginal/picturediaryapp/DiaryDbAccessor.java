package com.lyricaloriginal.picturediaryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LyricalMaestro on 15/01/24.
 */
public class DiaryDbAccessor {

    private DiaryDbAccessor() {
    }

    /**
     * 絵日記データを新規に追加します。
     *
     * @param context
     * @param model
     * @return
     */
    public static boolean insert(Context context, DiaryActivity.DiaryModel model) {
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

    public static List<DiaryInfo> loadDiaryInfoList(Context context){
        DiaryDbOpenHelper helper = new DiaryDbOpenHelper(context);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        List<DiaryInfo> diaryInfos = new ArrayList<DiaryInfo>();

        SQLiteDatabase db = null;
        Cursor cr = null;
        try {
            db = helper.getReadableDatabase();
            cr = db.query("T_DIARY",null, null, null ,null, null, "DATE");
            if(cr.moveToFirst()){
                do{
                    Date date = null;
                    try{
                        date = format.parse(CursorUtils.getString(cr, "DATE"));
                    }catch(ParseException ex){
                    }
                    String title = CursorUtils.getString(cr, "TITLE");
                    DiaryInfo dInfo = new DiaryInfo(date, title);
                    diaryInfos.add(dInfo);
                }while(cr.moveToNext());
            }
        } finally {
            if(cr != null){
                cr.close();
                cr = null;
            }
            if (db != null) {
                db.close();
                db = null;
            }
        }
        return diaryInfos;
    }
}
