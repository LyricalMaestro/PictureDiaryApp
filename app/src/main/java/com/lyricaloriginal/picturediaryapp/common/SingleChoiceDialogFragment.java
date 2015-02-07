package com.lyricaloriginal.picturediaryapp.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by LyricalMaestro on 15/02/07.
 */
public class SingleChoiceDialogFragment extends DialogFragment {

    public static interface Listener {

        public void onClick(String tag, int which, Bundle data);
    }

    private Listener _listener = null;

    public static DialogFragment newInstance(CharSequence[] choice, int checkedItem, Bundle data) {
        Bundle arg = new Bundle();
        arg.putCharSequenceArray("CHOICE", choice);
        arg.putInt("CHECKED_ITEM", checkedItem);
        if (data != null) {
            arg.putBundle("DATA", data);
        }

        DialogFragment f = new SingleChoiceDialogFragment();
        f.setArguments(arg);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Listener) {
            _listener = (Listener) activity;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CharSequence[] choice = getArguments().getCharSequenceArray("CHOICE");
        int which = getArguments().getInt("CHECKED_ITEM");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setSingleChoiceItems(choice, which, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Listener l = getListener();
                if (l != null) {
                    Bundle data = null;
                    if (getArguments().containsKey("DATA")) {
                        data = getArguments().getBundle("DATA");
                    }

                    l.onClick(getTag(), which, data);
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _listener = null;
    }

    private Listener getListener() {
        Fragment f = getTargetFragment();
        if (f != null && f instanceof Listener) {
            return (Listener) f;
        }

        return _listener;
    }
}
