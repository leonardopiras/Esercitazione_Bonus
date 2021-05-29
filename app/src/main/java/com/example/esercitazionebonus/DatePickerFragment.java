package com.example.esercitazionebonus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    private Calendar date;

    public static com.example.esercitazionebonus.DatePickerFragment newInstance(){
        com.example.esercitazionebonus.DatePickerFragment frag = new com.example.esercitazionebonus.DatePickerFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return  frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final DatePicker datePicker = new DatePicker(getActivity());
        date = Calendar.getInstance();

        return new AlertDialog.Builder(getActivity())
                .setView(datePicker)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        date.set(Calendar.YEAR, datePicker.getYear());
                        date.set(Calendar.MONTH, datePicker.getMonth());
                        date.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        ((Registrazione)getActivity()).doPositiveClick(date);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
    }

    public Calendar getDate() {
        return date;
    }
    public void setDate(Calendar date) {
        this.date = date;
    }
}
