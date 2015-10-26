package edu.osu.cse.todolist.to_dolist;

import android.app.Activity;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

/**
 * Created by AtwoodWang on 15/10/25.
 */
public class DateTimePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    private static final String ARG_TIME = "time";
    public static final String EXTRA_DATE = "edu.osu.cse.todolist.to_dolist";
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private int mHour;
    private int mMinute;


    public static DateTimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DateTimePickerFragment fragment = new DateTimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);


        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            mHour = mTimePicker.getHour();
                            mMinute = mTimePicker.getMinute();
                        } else {
                            mHour = mTimePicker.getCurrentHour();
                            mMinute = mTimePicker.getCurrentMinute();
                        }
                        Date date = new GregorianCalendar(year, month, day,mHour,mMinute).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })

                .setNegativeButton(android.R.string.cancel, null)
                .create();

    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}