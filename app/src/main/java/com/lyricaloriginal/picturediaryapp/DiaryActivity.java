package com.lyricaloriginal.picturediaryapp;

import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lyricaloriginal.picturediaryapp.common.CursorUtils;
import com.lyricaloriginal.picturediaryapp.common.DatePickerDialogFragment;
import com.lyricaloriginal.picturediaryapp.common.SingleChoiceDialogFragment;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 絵日記を作成する画面に対応するActivityです。
 */
public class DiaryActivity extends ActionBarActivity
        implements DatePickerDialogFragment.Listener, SingleChoiceDialogFragment.Listener {

    public static final String EXTRA_ID = "ID";

    private static final int NEW_DIARY_ID = -1; //  新規作成モードの時のIDの値。

    private int _targetId = NEW_DIARY_ID;

    private File _newFile = null;
    private Uri _uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_diary);

            initToolbar();
            setEvents();

            _targetId = getIntent().getIntExtra(EXTRA_ID, NEW_DIARY_ID);
            if (savedInstanceState == null) {
                if (0 < _targetId) {
                    setDataForNewMode();
                } else {
                    setDataForUpdateMode();
                }
            } else {
                String date = savedInstanceState.getString("DATE");
                TextView dateText = (TextView) findViewById(R.id.dateText);
                dateText.setText(date);

                String pictureFilename = savedInstanceState.getString("PICTURE_FILENAME");
                if (!TextUtils.isEmpty(pictureFilename)) {
                    _newFile = new File(pictureFilename);
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageURI(Uri.fromFile(_newFile));
                }
                _uri = savedInstanceState.getParcelable("URI");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri uri = _uri;
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(uri);
            _newFile = new File(uri.getPath());
            _uri = null;
        } else if (resultCode == RESULT_OK && requestCode == 2) {
            Uri uri = data.getData();
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(uri);
            getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            _newFile = getFile(getContentResolver(), uri);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            try {
                FileUtils.deleteDirectory(getTempPicDir());
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
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

                if (_newFile != null) {
                    model.pictureFile = _newFile;
                }

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

        if (_newFile != null) {
            outState.putString("PICTURE_FILENAME", _newFile.getAbsolutePath());
        }
        if (_uri != null) {
            outState.putParcelable("URI", _uri);
        }
    }

    @Override
    public void onDateCommitted(String tag, int year, int monthOfYear, int dayOfMonth) {
        String txt = String.format("%1$04d年%2$02d月%3$02d日", year, monthOfYear + 1, dayOfMonth);
        TextView dateText = (TextView) findViewById(R.id.dateText);
        dateText.setText(txt);
    }


    @Override
    public void onClick(String tag, int which, Bundle data) {
        if (TextUtils.equals(tag, SingleChoiceDialogFragment.class.getName())) {
            DialogFragment dialog = (DialogFragment) getFragmentManager().findFragmentByTag(tag);
            dialog.dismiss();

            if (which == 0) {
                File f = new File(getTempPicDir(), System.currentTimeMillis() + ".jpg");
                _uri = Uri.fromFile(f);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, _uri);
                startActivityForResult(intent, 1);
            } else if (which == 1) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            } else if (which == 2) {
            } else if (which == 3) {
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(null);
                _newFile = null;
            }
        }
    }

    private void setEvents() {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_newFile == null) {
                    CharSequence[] choice = getResources().getTextArray(R.array.pictureImportMenus);
                    DialogFragment dialog = SingleChoiceDialogFragment.newInstance(choice, -1, null);
                    dialog.show(getFragmentManager(), SingleChoiceDialogFragment.class.getName());
                } else {
                    Uri uri = Uri.fromFile(_newFile);
                    String ext = FilenameUtils.getExtension(_newFile.getName());
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, mimeType);
                    startActivity(intent);
                }
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (_newFile != null) {
                    CharSequence[] choice = getResources().getTextArray(R.array.pictureSelectedMenus);
                    DialogFragment dialog = SingleChoiceDialogFragment.newInstance(choice, -1, null);
                    dialog.show(getFragmentManager(), SingleChoiceDialogFragment.class.getName());
                    return true;
                }
                return false;
            }
        });

        TextView txtView = (TextView) findViewById(R.id.dateText);
        txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView dateText = (TextView) findViewById(R.id.dateText);
                String date = dateText.getText().toString();
                int year = Integer.valueOf(date.substring(0, 4));
                int monthOfYear = Integer.valueOf(date.substring(5, 7));
                int dayOfMonth = Integer.valueOf(date.substring(8, 10));

                DialogFragment dialog = DatePickerDialogFragment.newInstance(year, monthOfYear - 1, dayOfMonth);
                dialog.show(getFragmentManager(), dialog.getClass().getName());
            }
        });
    }

    private void setDataForUpdateMode() {
        //  新規作成時は日付として「今日の日付」で補填する
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String date = format.format(now);
        TextView dateText = (TextView) findViewById(R.id.dateText);
        dateText.setText(date);
    }

    private void setDataForNewMode() throws IOException {
        DiaryModel model = loadDiary(_targetId);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String date = format.format(model.date);
        TextView dateText = (TextView) findViewById(R.id.dateText);
        dateText.setText(date);

        EditText titleEditText = (EditText) findViewById(R.id.titleEditText);
        titleEditText.setText(model.title);

        EditText noteEditText = (EditText) findViewById(R.id.noteText);
        noteEditText.setText(model.note);

        if (model.pictureFile != null) {
            File pic = new File(getTempPicDir(), model.pictureFile.getName());
            FileUtils.copyFile(model.pictureFile, pic);
            _newFile = pic;
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(Uri.fromFile(_newFile));
        }
    }

    private DiaryModel loadDiary(int targetId) {
        DiaryModel model = DiaryDbAccessor.loadDiary(this, targetId);
        if (model == null) {
            throw new RuntimeException("指定したIDに対応する絵日記が存在しません。");
        }
        return model;
    }

    private void saveDiary(DiaryModel model) {
        boolean result = false;
        if (_targetId < 0) {
            result = DiaryDbAccessor.insert(this, model);
        } else {
            result = DiaryDbAccessor.update(this, _targetId, model);
        }

        if (result) {
            Toast.makeText(this, "保存成功！！", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
    }


    private File getFile(ContentResolver resolver, Uri uri) {
        File f = null;
        Cursor cr = null;
        try {
            cr = resolver.query(uri, new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (cr.moveToFirst()) {
                do {
                    f = new File(cr.getString(0));
                } while (cr.moveToNext());
            }
        } finally {
            CursorUtils.close(cr);
        }
        return f;
    }

    private void initToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);
    }

    private File getTempPicDir() {
        File dir = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "Temp");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

}
