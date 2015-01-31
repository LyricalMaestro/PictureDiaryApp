package com.lyricaloriginal.picturediaryapp;

import java.util.Date;

/**
 * 絵日記の基本情報を保持するためのクラスです。
 * <p/>
 * Created by LyricalMaestro on 15/01/11.
 */
public class DiaryInfo {
    private final int _id;
    private final Date _date;
    private final String _title;

    /**
     * コンストラクタ
     *
     * @param id    絵日記のID
     * @param date  日付
     * @param title タイトル
     */
    public DiaryInfo(int id, Date date, String title) {
        if (id < 0) {
            throw new IllegalArgumentException("第一引数idは0以上の整数を指定してください。");
        }
        if (date == null) {
            throw new IllegalArgumentException("第二引数Dateはnull以外のオブジェクトを指定していください。");
        }

        _id = id;
        _date = date;
        _title = title;
    }

    /**
     * 絵日記のIDを取得します。
     *
     * @return 絵日記のID
     */
    public int getId() {
        return _id;
    }

    public Date getDate() {
        return _date;
    }

    public String getTitle() {
        return _title;
    }
}
