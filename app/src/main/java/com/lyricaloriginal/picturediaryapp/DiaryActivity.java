package com.lyricaloriginal.picturediaryapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;

/**
 * 絵日記を作成する画面に対応するActivityです。
 */
public class DiaryActivity extends ActionBarActivity {

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

        _targetId = getIntent().getIntExtra(EXTRA_ID, NEW_DIARY_ID);
        if (savedInstanceState == null && 0 < _targetId) {
            loadDiary(_targetId);
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
        getMenuInflater().inflate(R.menu.menu_dialy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected DiaryModel loadDiary(int targetId) {
        return null;
    }
}
