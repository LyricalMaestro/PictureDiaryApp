package com.lyricaloriginal.picturediaryapp.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyricaloriginal.picturediaryapp.R;

/**
 * 確認ダイアログフラグメントです。
 */
public class ConfirmDialogFragment extends DialogFragment {

    public static interface Listener {
        public void onClickOkBtnListener(String tag, Bundle bundle);
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

    public static DialogFragment newInstance(String msg, Bundle bundle) {
        Bundle arg = new Bundle();
        arg.putString("MSG", msg);
        if (bundle != null) {
            arg.putBundle("BUNDLE", bundle);
        }
        DialogFragment f = new ConfirmDialogFragment();
        f.setArguments(arg);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getArguments().getString("MSG"));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (_listener != null) {
                    Bundle bundle = getArguments().getBundle("BUNDLE");
                    _listener.onClickOkBtnListener(getTag(), bundle);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }
}
