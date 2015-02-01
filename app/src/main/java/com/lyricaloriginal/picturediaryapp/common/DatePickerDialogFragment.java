package com.lyricaloriginal.picturediaryapp.common;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.DatePicker;

/**
 * 日付設定のためのDialogFragmentです。
 */
public class DatePickerDialogFragment extends DialogFragment {

    public static interface Listener {
        public void onDateCommitted(String tag, int year, int monthOfYear, int dayOfMonth);
    }

    private Listener _listener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Listener) {
            _listener = (Listener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _listener = null;
    }

    public static DialogFragment newInstance(int year, int monthOfYear, int dayOfMonth) {
        Bundle bundle = new Bundle();
        bundle.putInt("YEAR", year);
        bundle.putInt("MONTH_OF_YEAR", monthOfYear);
        bundle.putInt("DAY_OF_MONTH", dayOfMonth);

        DialogFragment f = new DatePickerDialogFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = getArguments().getInt("YEAR");
        int monthOfYear = getArguments().getInt("MONTH_OF_YEAR");
        int dayOfMonth = getArguments().getInt("DAY_OF_MONTH");
        return new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (_listener != null) {
                    _listener.onDateCommitted(getTag(), year, monthOfYear, dayOfMonth);
                }
            }
        }, year, monthOfYear, dayOfMonth);
    }
}
