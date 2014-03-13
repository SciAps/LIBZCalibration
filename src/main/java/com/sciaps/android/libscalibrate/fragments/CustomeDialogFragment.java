package com.sciaps.android.libscalibrate.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sciaps.android.libscalibrate.R;


public class CustomeDialogFragment extends DialogFragment {
    // Debugging
    private static final String TAG = "CustomeDialogFragment";
    private static final boolean D = true;

    // Return Intent extra

    private static String title;
    private static String message;


    public CustomeDialogFragment(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public CustomeDialogFragment(int title, int message) {


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();


        View v = inflater.inflate(R.layout.custome_progress_dialog, null);
        ((TextView) v.findViewById(R.id.header)).setText(title);
        builder.setView(v)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNeutralButton(R.string.skip, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }

}
