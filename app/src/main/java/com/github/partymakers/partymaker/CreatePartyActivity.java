package com.github.partymakers.partymaker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.github.partymakers.partymaker.databinding.ActivityCreatePartyBinding;

import java.util.Calendar;

public class CreatePartyActivity extends AppCompatActivity implements View.OnClickListener {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private ActivityCreatePartyBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCreatePartyBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);

        viewBinding.buttonDatePicker.setOnClickListener(this);
        viewBinding.buttonTimePicker.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == viewBinding.buttonDatePicker) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    viewBinding.textDatePicked.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == viewBinding.buttonTimePicker) {
            // Get Current Time
            final Calendar cal = Calendar.getInstance();
            mHour = cal.get(Calendar.HOUR_OF_DAY);
            mMinute = cal.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {
                    viewBinding.textTimePicked.setText(hourOfDay + ":" + minute);
                }
            }, mHour, mMinute, false);
            timePickerDialog.show();

            // TODO: date and time validation
        }
    }
}
