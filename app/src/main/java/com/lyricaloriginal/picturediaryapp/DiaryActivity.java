package com.lyricaloriginal.picturediaryapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 絵日記を作成する画面に対応するActivityです。
 */
public class DiaryActivity extends ActionBarActivity {

    /**
     * 絵日記オブジェクト。絵日記のデータを保持するためのクラス。
     */
    protected final class DiaryModel {
        protected String title;
        protected Date date;
        protected String note;
        protected File pictureFile;
    }

    public static final String EXTRA_ID = "ID";

    private static final int NEW_DIARY_ID = -1; //  新規作成モードの時のIDの値。

    private int _targetId = NEW_DIARY_ID;

    private Uri _uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String filename = System.currentTimeMillis() + ".jpg";
                File f = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), filename);
                _uri = Uri.fromFile(f);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, _uri);
                startActivityForResult(intent, 1);
            }
        });

        initToolbar();

        _targetId = getIntent().getIntExtra(EXTRA_ID, NEW_DIARY_ID);
        if (savedInstanceState == null) {
            if (0 < _targetId) {
                loadDiary(_targetId);
            } else {
                //  新規作成時は日付として「今日の日付」で補填する
                Date now = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                String date = format.format(now);
                TextView dateText = (TextView) findViewById(R.id.dateText);
                dateText.setText(date);
            }
        } else {
            String date = savedInstanceState.getString("DATE");
            TextView dateText = (TextView) findViewById(R.id.dateText);
            dateText.setText(date);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri uri = _uri;
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(uri);
            _uri = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add("保存");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DiaryModel model = new DiaryModel();

                EditText titleView = (EditText) findViewById(R.id.titleEditText);
                model.title = titleView.getEditableText().toString();

                TextView dateText = (TextView) findViewById(R.id.dateText);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                try {
                    model.date = format.parse(dateText.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                EditText noteText = (EditText) findViewById(R.id.noteText);
                model.note = noteText.getEditableText().toString();

                saveDiary(model);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView dateText = (TextView) findViewById(R.id.dateText);
        outState.putString("DATE", dateText.getText().toString());
    }

    protected DiaryModel loadDiary(int targetId) {
        return null;
    }

    protected void saveDiary(DiaryModel model) {
        if (_targetId < 0) {
            boolean result = DiaryDbAccessor.insert(this, model);
            if (result) {
                Toast.makeText(this, "保存成功！！", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    private void initToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);
    }
}
