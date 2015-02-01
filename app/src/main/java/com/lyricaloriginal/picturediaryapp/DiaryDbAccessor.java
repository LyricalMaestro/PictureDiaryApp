package com.lyricaloriginal.picturediaryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;

import com.lyricaloriginal.picturediaryapp.common.CursorUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
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
    public static boolean insert(Context context, DiaryModel model) {
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
            cv.put("PICTURE_FILE",
                    model.pictureFile != null ? model.pictureFile.getName() : "");
            result = db.insert("T_DIARY", "", cv);

            if (0 <= result && model.pictureFile != null) {
                result = copyPictureFile(context, (int) result, model.pictureFile);
            }

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

    /**
     * 指定したIDの絵日記データの更新を行います。
     *
     * @param context
     * @param id
     * @param model
     * @return
     */
    public static boolean update(Context context, int id, DiaryModel model) {
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
            cv.put("PICTURE_FILE",
                    model.pictureFile != null ? model.pictureFile.getName() : "");
            result = db.update("T_DIARY", cv, "_id=" + id, null);

            if (0 <= result && model.pictureFile != null) {
                result = copyPictureFile(context, id, model.pictureFile);
            } else if (0 <= result) {
                result = deletePic(context, id);
            }

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

    /**
     * 指定したIDの絵日記データを削除します。
     *
     * @param context
     * @param id
     * @return
     */
    public static boolean delete(Context context, int id) {
        long result = -1;
        DiaryDbOpenHelper helper = new DiaryDbOpenHelper(context);
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();

            result = db.delete("T_DIARY", "_id=" + id, null);
            if (0 <= result) {
                result = deletePic(context, id);
            }
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

    /**
     * 登録されている全絵日記の基本情報を取得します。
     *
     * @param context
     * @return
     */
    public static List<DiaryInfo> loadDiaryInfoList(Context context) {
        DiaryDbOpenHelper helper = new DiaryDbOpenHelper(context);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        List<DiaryInfo> diaryInfos = new ArrayList<DiaryInfo>();

        SQLiteDatabase db = null;
        Cursor cr = null;
        try {
            db = helper.getReadableDatabase();
            cr = db.query("T_DIARY", null, null, null, null, null, "DATE");
            if (cr.moveToFirst()) {
                do {
                    int id = CursorUtils.getInt(cr, "_id");
                    Date date = null;
                    try {
                        date = format.parse(CursorUtils.getString(cr, "DATE"));
                    } catch (ParseException ex) {
                    }
                    String title = CursorUtils.getString(cr, "TITLE");
                    DiaryInfo dInfo = new DiaryInfo(id, date, title);
                    diaryInfos.add(dInfo);
                } while (cr.moveToNext());
            }
        } finally {
            CursorUtils.close(cr);
            if (db != null) {
                db.close();
                db = null;
            }
        }
        return diaryInfos;
    }

    public static DiaryModel loadDiary(Context context, int id) {
        DiaryDbOpenHelper helper = new DiaryDbOpenHelper(context);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        DiaryModel diaryModel = new DiaryModel();

        SQLiteDatabase db = null;
        Cursor cr = null;
        try {
            db = helper.getReadableDatabase();
            cr = db.query("T_DIARY", null, "_id=" + id, null, null, null, null);
            if (cr.moveToFirst() && cr.getCount() == 1) {
                try {
                    diaryModel.date = format.parse(CursorUtils.getString(cr, "DATE"));
                } catch (ParseException ex) {
                }
                diaryModel.title = CursorUtils.getString(cr, "TITLE");
                diaryModel.note = CursorUtils.getString(cr, "NOTE");
                String picFilename = CursorUtils.getString(cr, "PICTURE_FILE");
                if (!TextUtils.isEmpty(picFilename)) {
                    diaryModel.pictureFile = new File(getPicDir(context, id), picFilename);
                }
            }
        } finally {
            CursorUtils.close(cr);
            if (db != null) {
                db.close();
                db = null;
            }
        }
        return diaryModel;
    }

    private static File getPicDir(Context context, int id) {
        return new File(context.
                getExternalFilesDir(Environment.DIRECTORY_DCIM),
                String.valueOf(id));
    }

    private static int copyPictureFile(Context context, int id, File orgFile) {
        File destDir = getPicDir(context, id);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        File destFile = new File(destDir, orgFile.getName());
        int result = -1;
        try {
            FileUtils.copyFile(orgFile, destFile);
            result = 1;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private static int deletePic(Context context, int id) {
        File destDir = getPicDir(context, id);
        if (!destDir.exists()) {
            return 1;
        }

        int result = -1;
        try {
            FileUtils.deleteDirectory(destDir);
            result = 1;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
