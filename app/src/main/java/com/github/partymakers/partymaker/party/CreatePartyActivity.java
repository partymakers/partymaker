package com.github.partymakers.partymaker.party;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.github.partymakers.partymaker.R;
import com.github.partymakers.partymaker.databinding.ActivityCreatePartyBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreatePartyActivity extends AppCompatActivity implements View.OnClickListener {
    private int year, month, day, hour, minute;
    private ActivityCreatePartyBinding viewBinding;

    private List<String> tagListFood = Arrays.asList("Polish", "Pizza", "Kebab", "Sushi", "Asian", "Italian", "Burgers", "Mexican", "Vietnamese"); //make it final?
    private List<String> tagListDrinks = Arrays.asList("Soda", "Light beer", "Craft beer", "Cocktail", "Juice", "Soft drinks", "Vodka", "Whiskey", "Martini", "Shots", "Wine", "Tea", "Coffee");
    final List<String> currencies = Arrays.asList("PLN", "USD", "EUR");
    private Set<String> checkedChipsFood = new HashSet<String>();
    private Set<String> checkedChipsDrinks = new HashSet<String>();

    // TODO: text input check and set errors https://codelabs.developers.google.com/codelabs/mdc-111-kotlin/#2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCreatePartyBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);
        setTag(tagListFood, viewBinding.foodChipGroup);
        setTag(tagListDrinks, viewBinding.drinksChipGroup);

        viewBinding.textDatePicked.setOnClickListener(this);
        viewBinding.textTimePicked.setOnClickListener(this);
        viewBinding.foodChipButton.setOnClickListener(this);
        viewBinding.drinksChipButton.setOnClickListener(this);
        viewBinding.submitButton.setOnClickListener(this);

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
                    viewBinding.foodChipGroup.setVisibility(View.VISIBLE);
                    viewBinding.foodInputLayout.setVisibility(View.VISIBLE);
                    viewBinding.textInputFood.setVisibility(View.VISIBLE);
                    viewBinding.foodChipButton.setVisibility(View.VISIBLE);
                    viewBinding.foodChipInputLayout.setVisibility(View.VISIBLE);
                } else {
                    // If the switch button is off
                    viewBinding.foodChipGroup.setVisibility(View.GONE);
                    viewBinding.foodInputLayout.setVisibility(View.GONE);
                    viewBinding.textInputFood.setVisibility(View.GONE);
                    viewBinding.foodChipButton.setVisibility(View.GONE);
                    viewBinding.foodChipInputLayout.setVisibility(View.GONE);
                }
            }

        });

        viewBinding.switchDrinks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    viewBinding.drinksChipGroup.setVisibility(View.VISIBLE);
                    viewBinding.drinksInputLayout.setVisibility(View.VISIBLE);
                    viewBinding.textInputDrinks.setVisibility(View.VISIBLE);
                    viewBinding.drinksChipButton.setVisibility(View.VISIBLE);
                    viewBinding.drinksChipInputLayout.setVisibility(View.VISIBLE);
                } else {
                    // If the switch button is off
                    viewBinding.drinksChipGroup.setVisibility(View.GONE);
                    viewBinding.drinksInputLayout.setVisibility(View.GONE);
                    viewBinding.textInputDrinks.setVisibility(View.GONE);
                    viewBinding.drinksChipButton.setVisibility(View.GONE);
                    viewBinding.drinksChipInputLayout.setVisibility(View.GONE);
                }
            }

        });

        viewBinding.switchFee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    viewBinding.feeLinearLayout.setVisibility(View.VISIBLE);
                    viewBinding.feeInputLayout.setVisibility(View.VISIBLE);
                    viewBinding.textInputFee.setVisibility(View.VISIBLE);
                } else {
                    // If the switch button is off
                    viewBinding.feeLinearLayout.setVisibility(View.GONE);
                    viewBinding.feeInputLayout.setVisibility(View.GONE);
                    viewBinding.textInputFee.setVisibility(View.GONE);
                }
            }

        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, currencies);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewBinding.feeSpinner.setAdapter(arrayAdapter);

        viewBinding.feeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Currency")) { //might be useful later
                } else {
                    //String item = parent.getItemAtPosition(position).toString();  //might be useful later
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { //might be useful later
            }
        });

        viewBinding.switchParking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    viewBinding.parkingInputLayout.setVisibility(View.VISIBLE);
                    viewBinding.textInputParking.setVisibility(View.VISIBLE);
                } else {
                    // If the switch button is off
                    viewBinding.parkingInputLayout.setVisibility(View.GONE);
                    viewBinding.textInputParking.setVisibility(View.GONE);
                }
            }

        });

    }

    //onclick listeners for date/time pickers, food/... chip input
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
        } else if (v == viewBinding.textTimePicked) {
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
        } else if (v == viewBinding.foodChipButton) {
            if (!viewBinding.textInputFood.getText().toString().trim().isEmpty() && !chipExists(viewBinding.textInputFood.getText().toString(), viewBinding.foodChipGroup)) {
                Chip newChip = new Chip(CreatePartyActivity.this);
                ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Choice);  //change to CustomChip style in the future
                newChip.setChipDrawable(drawable);
                newChip.setText(viewBinding.textInputFood.getText().toString());
                newChip.setChecked(true);
                viewBinding.foodChipGroup.addView(newChip);
            }
            viewBinding.textInputFood.setText("");
        } else if (v == viewBinding.drinksChipButton) {
            if (!viewBinding.textInputDrinks.getText().toString().trim().isEmpty() && !chipExists(viewBinding.textInputDrinks.getText().toString(), viewBinding.drinksChipGroup)) {
                Chip newChip = new Chip(CreatePartyActivity.this);
                ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Choice);  //change to CustomChip style in the future
                newChip.setChipDrawable(drawable);
                newChip.setText(viewBinding.textInputDrinks.getText().toString());
                newChip.setChecked(true);
                viewBinding.drinksChipGroup.addView(newChip);
            }
            viewBinding.textInputDrinks.setText("");
        } else if (v == viewBinding.submitButton) {
            for (int i = 0; i < viewBinding.foodChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) viewBinding.foodChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    checkedChipsFood.add(chip.getText().toString());
                }
            }

            for (int i = 0; i < viewBinding.drinksChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) viewBinding.drinksChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    checkedChipsDrinks.add(chip.getText().toString());
                }
            }

        }
    }


    private void setTag(final List<String> tagList, ChipGroup chipGroup) {
        for (int index = 0; index < tagList.size(); index++) {
            final String tagName = tagList.get(index);
            final Chip chip = new Chip(this);
            ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Choice);  //change to CustomChip style in the future
            chip.setChipDrawable(drawable);

            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
            chip.setText(tagName);
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

    //checks if chip with text already exists in the chipGroup
    private Boolean chipExists(String text, ChipGroup chipGroup) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (text.equals(chip.getText().toString())) {
                chip.setChecked(true);
                return true;
            }
        }
        return false;
    }
}
