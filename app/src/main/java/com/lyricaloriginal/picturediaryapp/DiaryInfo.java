package com.lyricaloriginal.picturediaryapp;

import java.util.Date;

/**
 * Created by shinichitanimoto on 15/01/11.
 */
public class DiaryInfo {
    private Date _date;
    private String _title;

    /**
     * コンストラクタ
     *
     * @param date
     * @param title
     */
    public DiaryInfo(Date date, String title) {
        if (date == null) {
            throw new IllegalArgumentException("第一引数Dateはnull以外のオブジェクトを指定していください。");
        }

        _date = date;
        _title = title;
    }

    public Date getDate() {
        return _date;
    }

    public String getTitle() {
        return _title;
    }
}
