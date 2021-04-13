package com.github.partymakers.partymaker.party;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.github.partymakers.partymaker.databinding.ActivityCreatePartyBinding;

import java.util.Calendar;

public class CreatePartyActivity extends AppCompatActivity implements View.OnClickListener {
    private int year, month, day, hour, minute;
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
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    viewBinding.textDatePicked.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                }
            }, year, month, day);
            datePickerDialog.show();
        }

        if (v == viewBinding.buttonTimePicker) {
            // Get Current Time
            final Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {
                    viewBinding.textTimePicked.setText(hourOfDay + ":" + minute);
                }
            }, hour, minute, false);
            timePickerDialog.show();

            // TODO: date and time validation
        }
    }
}
