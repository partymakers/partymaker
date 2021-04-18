package com.github.partymakers.partymaker.party;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TimePicker;

import com.github.partymakers.partymaker.R;
import com.github.partymakers.partymaker.databinding.ActivityCreatePartyBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CreatePartyActivity extends AppCompatActivity implements View.OnClickListener {
    private int year, month, day, hour, minute;
    private ActivityCreatePartyBinding viewBinding;
    Switch dressCode = null;
    final List<String> tagList = Arrays.asList("Polish", "Pizza", "Kebab", "Sushi", "Asian", "Italian", "Burgers", "Mexican", "Vietnamese");

    // TODO: text input check and set errors https://codelabs.developers.google.com/codelabs/mdc-111-kotlin/#2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCreatePartyBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);
        setTag(tagList);

        viewBinding.textDatePicked.setOnClickListener(this);
        viewBinding.textTimePicked.setOnClickListener(this);

        viewBinding.switchDressCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    viewBinding.dressCodeInputLayout.setVisibility(View.VISIBLE);
                    viewBinding.textInputDressCode.setVisibility(View.VISIBLE);
                } else {
                    // If the switch button is off
                    viewBinding.dressCodeInputLayout.setVisibility(View.GONE);
                    viewBinding.textInputDressCode.setVisibility(View.GONE);
                }
            }

        });

        viewBinding.switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    viewBinding.themeInputLayout.setVisibility(View.VISIBLE);
                    viewBinding.textInputTheme.setVisibility(View.VISIBLE);
                } else {
                    // If the switch button is off
                    viewBinding.themeInputLayout.setVisibility(View.GONE);
                    viewBinding.textInputTheme.setVisibility(View.GONE);
                }
            }

        });

        viewBinding.switchFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    viewBinding.chipGroup.setVisibility(View.VISIBLE);
                    viewBinding.foodInputLayout.setVisibility(View.VISIBLE);
                    viewBinding.textInputFood.setVisibility(View.VISIBLE);
                } else {
                    // If the switch button is off
                    viewBinding.chipGroup.setVisibility(View.GONE);
                    viewBinding.foodInputLayout.setVisibility(View.GONE);
                    viewBinding.textInputFood.setVisibility(View.GONE);
                }
            }

        });
    }

    @Override
    public void onClick(View v) {

        if (v == viewBinding.textDatePicked) {
            // Get Current Date
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    viewBinding.textDatePicked.setText(dayOfMonth + "-" + ((monthOfYear + 1) <= 9 ? "0" + (monthOfYear + 1) : String.valueOf((monthOfYear + 1))) + "-" + year);
                }
            }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); //sets today's date as minimum date -> all the past dates are disabled
            datePickerDialog.show();
        }

        if (v == viewBinding.textTimePicked) {
            // Get Current Time
            final Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {
                    viewBinding.textTimePicked.setText(hourOfDay + ":" + (minute <= 9 ? "0" + minute : String.valueOf(minute)));
                }
            }, hour, minute, false);
            timePickerDialog.show();

            // TODO: date and time validation
        }
    }


    private void setTag(final List<String> tagList) {
            final ChipGroup chipGroup = viewBinding.chipGroup;
            for (int index = 0; index < tagList.size(); index++) {
                final String tagName = tagList.get(index);
                final Chip chip = new Chip(this);
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                chip.setText(tagName);
                chip.setCloseIconEnabled(false);
                //Added click listener on close icon to remove tag from ChipGroup
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagList.remove(tagName);
                        chipGroup.removeView(chip);
                    }
                });

                chipGroup.addView(chip);
            }
        }
}
