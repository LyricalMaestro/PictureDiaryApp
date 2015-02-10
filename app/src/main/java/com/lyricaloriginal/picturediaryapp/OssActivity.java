package com.lyricaloriginal.picturediaryapp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * 使用しているオープンソースライブラリの情報を表示するためのActivityです。
 */
public class OssActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oss);
        InputStream is = null;
        try {
            is = getAssets().open("apache_license.txt");
            List<String> lines = IOUtils.readLines(is, "UTF-8");
            TextView v = (TextView) findViewById(R.id.apacheLicTextView);
            for (String l : lines) {
                v.append(l + "\n");
            }
        } catch (IOException ex) {
            Log.e("DIARY", ex.getMessage(), ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }


}
