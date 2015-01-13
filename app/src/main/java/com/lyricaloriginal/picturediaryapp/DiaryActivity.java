package com.lyricaloriginal.picturediaryapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 絵日記を作成する画面に対応するActivityです。
 */
public class DiaryActivity extends ActionBarActivity {

    public static final String EXTRA_ID = "ID";

    private static final int NEW_DIARY_ID = -1; //  新規作成モードの時のIDの値。

    private int _targetId = NEW_DIARY_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        _targetId = getIntent().getIntExtra(EXTRA_ID, NEW_DIARY_ID);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
}
