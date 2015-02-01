package com.lyricaloriginal.picturediaryapp.common;

import android.database.Cursor;

import java.io.IOException;

/**
 * Created by LyricalMaestro on 15/01/27.
 */
public final class CursorUtils {

    private CursorUtils() {

    }

    /**
     * 指定したカラムに対応する文字列値を取得します。
     *
     * @param cr      カーソルオブジェクト
     * @param colName カラム名
     * @return 文字列値。カーソル内に指定したカラムが存在しない場合はnull。
     */
    public static String getString(Cursor cr, String colName) {
        return getString(cr, colName, null);
    }

    /**
     * 指定したカラムに対応する文字列値を取得します。
     * カーソル内に指定したカラムが存在しない場合は「指定したデフォルト値」を返します。
     *
     * @param cr      カーソルオブジェクト
     * @param colName カラム名
     * @param defVal  デフォルト値
     * @return 文字列値。
     */
    public static String getString(Cursor cr, String colName, String defVal) {
        if (cr == null) {
            return defVal;
        }

        int index = cr.getColumnIndex(colName);
        if (index == -1) {
            return defVal;
        }

        return cr.getString(index);
    }

    public static Integer getInt(Cursor cr, String colName) {
        return getInt(cr, colName, null);
    }

    public static Integer getInt(Cursor cr, String colName, Integer defVal) {
        if (cr == null) {
            return defVal;
        }

        int index = cr.getColumnIndex(colName);
        if (index == -1) {
            return defVal;
        }

        return cr.getInt(index);
    }

    /**
     * Cursorオブジェクトのクローズ処理を行います。
     *
     * @param cr Cursorオブジェクト
     */
    public static void close(Cursor cr) {
        if (cr != null) {
            cr.close();
        }
    }
}
