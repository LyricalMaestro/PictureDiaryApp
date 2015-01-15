package com.lyricaloriginal.picturediaryapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 絵日記の一覧を取得します。
 */
public class DiaryListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DiaryInfo[] diaryInfos = createDiaryInfos();

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new DiaryListAdapter(this, diaryInfos));
    }

    private DiaryInfo[] createDiaryInfos() {
        return new DiaryInfo[]{
                new DiaryInfo(new Date(), "テスト"),
                new DiaryInfo(new Date(), "テスト2"),
                new DiaryInfo(new Date(), "今日は何の避難の日"),
                new DiaryInfo(new Date(), "")
        };
    }

    class DiaryListAdapter extends BaseAdapter {

        private final LayoutInflater _inflater;
        private final DiaryInfo[] _diaryInfos;
        // このクラス自体UIスレッドでのみ使うことを想定しているのでフィールドとして持っても問題無い
        private final SimpleDateFormat _format;

        /**
         * コンストラクタ
         *
         * @param context
         * @param diaryInfos
         */
        DiaryListAdapter(Context context, DiaryInfo[] diaryInfos) {
            _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _diaryInfos = diaryInfos;
            _format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        }

        @Override
        public int getCount() {
            return _diaryInfos.length;
        }

        @Override
        public Object getItem(int position) {
            return _diaryInfos[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = _inflater.inflate(R.layout.diary_info_item,null);
            }

            DiaryInfo info = _diaryInfos[position];
            TextView dateText = (TextView) convertView.findViewById(R.id.dateText);
            TextView titleText = (TextView) convertView.findViewById(R.id.titleText);
            dateText.setText(_format.format(info.getDate()));
            titleText.setText(info.getTitle());
            return convertView;
        }
    }
}
