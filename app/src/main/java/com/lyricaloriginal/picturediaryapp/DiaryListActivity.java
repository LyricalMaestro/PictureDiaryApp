package com.lyricaloriginal.picturediaryapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 絵日記の一覧を取得します。
 */
public class DiaryListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View buttonFloat = findViewById(R.id.buttonFloat);
        buttonFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryListActivity.this, DiaryActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        initToolbar();

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DiaryInfo dInfo = (DiaryInfo) parent.getAdapter().getItem(position);
                Intent intent = new Intent(DiaryListActivity.this, DiaryActivity.class);
                intent.putExtra(DiaryActivity.EXTRA_ID, dInfo.getId());
                startActivityForResult(intent, 0);
            }
        });


        loadDiaryInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            loadDiaryInfo();
        }
    }

    private void initToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);
    }

    private void loadDiaryInfo() {
        DiaryInfo[] diaryInfos = createDiaryInfos();

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new DiaryListAdapter(this, diaryInfos));
    }

    private DiaryInfo[] createDiaryInfos() {
        List<DiaryInfo> diaryInfos = DiaryDbAccessor.loadDiaryInfoList(this);
        return diaryInfos.toArray(new DiaryInfo[0]);
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
                convertView = _inflater.inflate(R.layout.diary_info_item, null);
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
