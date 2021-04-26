package com.github.partymakers.partymaker.party;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.github.partymakers.partymaker.R;
import com.github.partymakers.partymaker.databinding.ActivityCreatePartyBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CreatePartyActivity extends AppCompatActivity implements View.OnClickListener {
    static final private String TAG = CreatePartyActivity.class.getSimpleName();
    static private final List<String> tagListFood = Arrays.asList("Polish", "Pizza", "Kebab", "Sushi", "Asian", "Italian", "Burgers", "Mexican", "Vietnamese");
    static private final List<String> tagListDrinks = Arrays.asList("Soda", "Light beer", "Craft beer", "Cocktail", "Juice", "Soft drinks", "Vodka", "Whiskey", "Martini", "Shots", "Wine", "Tea", "Coffee");

    private ActivityCreatePartyBinding dataBinding;
    private FirebaseFirestore firestore;
    private CollectionReference partiesRepo;
    private PartyEntity party = new PartyEntity();

    // TODO: text input check and set errors https://codelabs.developers.google.com/codelabs/mdc-111-kotlin/#2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_party);
        setContentView(dataBinding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        partiesRepo = firestore.collection("parties");

        setTag(tagListFood, party.getFood(), dataBinding.foodChipGroup);
        setTag(tagListDrinks, party.getDrinks(), dataBinding.drinksChipGroup);


        dataBinding.textPartyName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setName(dataBinding.textPartyName.getText().toString());
            }
        });
        dataBinding.textPartyDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setDescription(dataBinding.textPartyDescription.getText().toString());
            }
        });
        dataBinding.textDatePicked.setOnClickListener(this);
        dataBinding.textTimePicked.setOnClickListener(this);
        dataBinding.textLocationPicked.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setLocation(dataBinding.textLocationPicked.getText().toString());
            }
        });
        dataBinding.textInputTheme.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setTheme(dataBinding.textInputTheme.getText().toString());
            }
        });
        dataBinding.textInputDressCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setDressCode(dataBinding.textInputDressCode.getText().toString());
            }
        });
        dataBinding.foodChipButton.setOnClickListener(this);
        dataBinding.drinksChipButton.setOnClickListener(this);
        dataBinding.textInputFee.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    parseFee();
                }
            }
        });
        dataBinding.checkBoxPartner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                party.setAllowsPartner(isChecked);
            }
        });

        dataBinding.checkBoxFriends.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                party.setAllowsFriends(isChecked);
            }
        });
        dataBinding.checkBoxChildren.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                party.setAllowsChildren(isChecked);
            }
        });
        dataBinding.checkBoxPets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                party.setAllowsPets(isChecked);
            }
        });
        dataBinding.textInputParking.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setParkingDetails(dataBinding.textInputParking.getText().toString());
            }
        });
        dataBinding.textInputInfo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setAdditionalInformation(dataBinding.textInputInfo.getText().toString());
            }
        });
        dataBinding.submitButton.setOnClickListener(this);

        dataBinding.switchDressCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    dataBinding.dressCodeInputLayout.setVisibility(View.VISIBLE);
                    dataBinding.textInputDressCode.setVisibility(View.VISIBLE);
                    party.setDressCode(dataBinding.textInputDressCode.getText().toString());
                } else {
                    // If the switch button is off
                    dataBinding.dressCodeInputLayout.setVisibility(View.GONE);
                    dataBinding.textInputDressCode.setVisibility(View.GONE);
                    party.setDressCode(null);
                }
            }

        });

        dataBinding.switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    dataBinding.themeInputLayout.setVisibility(View.VISIBLE);
                    dataBinding.textInputTheme.setVisibility(View.VISIBLE);
                    party.setTheme(dataBinding.textInputTheme.getText().toString());
                } else {
                    // If the switch button is off
                    dataBinding.themeInputLayout.setVisibility(View.GONE);
                    dataBinding.textInputTheme.setVisibility(View.GONE);
                    party.setTheme(null);
                }
            }

        });

        dataBinding.switchFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    dataBinding.foodChipGroup.setVisibility(View.VISIBLE);
                    dataBinding.foodInputLayout.setVisibility(View.VISIBLE);
                    dataBinding.textInputFood.setVisibility(View.VISIBLE);
                    dataBinding.foodChipButton.setVisibility(View.VISIBLE);
                    dataBinding.foodChipInputLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < dataBinding.foodChipGroup.getChildCount(); i++) {
                        Chip chip = (Chip) dataBinding.foodChipGroup.getChildAt(i);
                        if (chip.isChecked()) {
                            party.getFood().add(chip.getText().toString());
                        }
                    }
                } else {
                    // If the switch button is off
                    dataBinding.foodChipGroup.setVisibility(View.GONE);
                    dataBinding.foodInputLayout.setVisibility(View.GONE);
                    dataBinding.textInputFood.setVisibility(View.GONE);
                    dataBinding.foodChipButton.setVisibility(View.GONE);
                    dataBinding.foodChipInputLayout.setVisibility(View.GONE);
                    party.getFood().clear();
                }
            }

        });

        dataBinding.switchDrinks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    dataBinding.drinksChipGroup.setVisibility(View.VISIBLE);
                    dataBinding.drinksInputLayout.setVisibility(View.VISIBLE);
                    dataBinding.textInputDrinks.setVisibility(View.VISIBLE);
                    dataBinding.drinksChipButton.setVisibility(View.VISIBLE);
                    dataBinding.drinksChipInputLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < dataBinding.drinksChipGroup.getChildCount(); i++) {
                        Chip chip = (Chip) dataBinding.drinksChipGroup.getChildAt(i);
                        if (chip.isChecked()) {
                            party.getDrinks().add(chip.getText().toString());
                        }
                    }
                } else {
                    // If the switch button is off
                    dataBinding.drinksChipGroup.setVisibility(View.GONE);
                    dataBinding.drinksInputLayout.setVisibility(View.GONE);
                    dataBinding.textInputDrinks.setVisibility(View.GONE);
                    dataBinding.drinksChipButton.setVisibility(View.GONE);
                    dataBinding.drinksChipInputLayout.setVisibility(View.GONE);
                    party.getDrinks().clear();
                }
            }

        });

        dataBinding.switchFee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    dataBinding.feeLinearLayout.setVisibility(View.VISIBLE);
                    dataBinding.feeInputLayout.setVisibility(View.VISIBLE);
                    dataBinding.textInputFee.setVisibility(View.VISIBLE);
                    parseFee();
                } else {
                    // If the switch button is off
                    dataBinding.feeLinearLayout.setVisibility(View.GONE);
                    dataBinding.feeInputLayout.setVisibility(View.GONE);
                    dataBinding.textInputFee.setVisibility(View.GONE);
                    party.setFee(null);
                }
            }

        });

        dataBinding.switchParking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    dataBinding.parkingInputLayout.setVisibility(View.VISIBLE);
                    dataBinding.textInputParking.setVisibility(View.VISIBLE);
                    party.setParkingDetails(buttonView.getText().toString());
                } else {
                    // If the switch button is off
                    dataBinding.parkingInputLayout.setVisibility(View.GONE);
                    dataBinding.textInputParking.setVisibility(View.GONE);
                    party.setParkingDetails(null);
                }
            }

        });

    }

    private void parseFee() {
        String amountString = dataBinding.textInputFee.getText().toString();
        try {
            BigDecimal amount = new BigDecimal(amountString);
            party.setFee(amount);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "invalid fee");
            party.setFee(null);
        }
    }


    //onclick listeners for date/time pickers, food/... chip input
    @Override
    public void onClick(View v) {

        if (v == dataBinding.textDatePicked) {
            // Get Current Date
            final Calendar currentDate = Calendar.getInstance();
            int currentYear = currentDate.get(Calendar.YEAR);
            int currentMonth = currentDate.get(Calendar.MONTH);
            int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar date = Calendar.getInstance();
                    if (party.getTimestamp() != null) {
                        date.setTimeInMillis(party.getTimestamp());
                    }
                    date.set(year, monthOfYear, dayOfMonth);
                    party.setTimestamp(date.getTimeInMillis());

                    String dateText = SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(date.getTime());
                    dataBinding.textDatePicked.setText(dateText);
//                    viewBinding.textDatePicked.setText(dayOfMonth + "-" + ((monthOfYear + 1) <= 9 ? "0" + (monthOfYear + 1) : String.valueOf((monthOfYear + 1))) + "-" + year);
                }
            }, currentYear, currentMonth, currentDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); //sets today's date as minimum date -> all the past dates are disabled
            datePickerDialog.show();
        } else if (v == dataBinding.textTimePicked) {
            // Get Current Time
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);


            // Launch Time Picker Dialog
            new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Calendar time = Calendar.getInstance();
                    if (party.getTimestamp() != null) {
                        time.setTimeInMillis(party.getTimestamp());
                    }
                    time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    time.set(Calendar.MINUTE, minute);
                    party.setTimestamp(time.getTimeInMillis());
                    String timeText = SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(time.getTime());
                    dataBinding.textTimePicked.setText(timeText);
                }
            }, hour, minute, android.text.format.DateFormat.is24HourFormat(getApplicationContext())).show();
        } else if (v == dataBinding.foodChipButton) {
            String foodEntered = dataBinding.textInputFood.getText().toString();
            if (!foodEntered.trim().isEmpty() && !chipExists(foodEntered, dataBinding.foodChipGroup)) {
                Chip newChip = new Chip(CreatePartyActivity.this);
                ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Choice);  //change to CustomChip style in the future
                newChip.setChipDrawable(drawable);
                newChip.setText(foodEntered);
                party.getFood().add(foodEntered);
                newChip.setChecked(true);
                dataBinding.foodChipGroup.addView(newChip);
            }
            dataBinding.textInputFood.setText("");
        } else if (v == dataBinding.drinksChipButton) {
            String drinkEntered = dataBinding.textInputDrinks.getText().toString();
            if (!drinkEntered.trim().isEmpty() && !chipExists(drinkEntered, dataBinding.drinksChipGroup)) {
                Chip newChip = new Chip(CreatePartyActivity.this);
                ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Choice);  //change to CustomChip style in the future
                newChip.setChipDrawable(drawable);
                newChip.setText(drinkEntered);
                party.getDrinks().add(drinkEntered);
                newChip.setChecked(true);
                dataBinding.drinksChipGroup.addView(newChip);
            }
            dataBinding.textInputDrinks.setText("");
        } else if (v == dataBinding.submitButton) {
            partiesRepo.add(party)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.i(TAG, "Party persisted.");
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "Persisting party failed");
                            Snackbar.make(dataBinding.getRoot(), "Saving failed", Snackbar.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void setTag(final List<String> tagList, List<String> selectedTags, ChipGroup chipGroup) {
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
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedTags.add(buttonView.getText().toString());
                    } else {

                    }
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
