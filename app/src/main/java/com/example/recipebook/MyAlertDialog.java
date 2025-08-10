package com.example.recipebook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyAlertDialog extends DialogFragment {

    public static MyAlertDialog newInstance(int title, int massege){
        MyAlertDialog frag = new MyAlertDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putInt("ms", massege);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        int ms = getArguments().getInt("ms");
        boolean delete=false;
        int icon;
        if(title==R.string.delete) {
            icon = R.drawable.delete64;
            delete=true;
        }
        else {
            icon = R.drawable.poweroff;
            delete=false;
        }

        boolean finalDelete = delete;
        return new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                .setIcon(icon)
                .setTitle(title)
                .setMessage(ms)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(finalDelete)
                                    ((MainActivity) getActivity()).doPositiveClick();
                                else{
                                    ((All)getActivity()).exitApp();
                                }
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        }
                )
                .create();
    }
}
