package com.lyricaloriginal.picturediaryapp;

import android.database.Cursor;

/**
 * Created by LyricalMaestro on 15/01/27.
 */
public class CursorUtils {

    private CursorUtils(){

    }

    public static String getString(Cursor cr, String colName){
        return getString(cr, colName, null);
    }

    public static String getString(Cursor cr, String colName, String defVal){
        if(cr == null){
            return defVal;
        }

        int index = cr.getColumnIndex(colName);
        if(index == -1){
            return defVal;
        }

        return cr.getString(index);
    }
}
