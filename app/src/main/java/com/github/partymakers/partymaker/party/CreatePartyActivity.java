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

    private ActivityCreatePartyBinding viewBinding;
    private FirebaseFirestore firestore;
    private CollectionReference partiesRepo;
    private PartyEntity party = new PartyEntity();

    // TODO: text input check and set errors https://codelabs.developers.google.com/codelabs/mdc-111-kotlin/#2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCreatePartyBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);

        firestore = FirebaseFirestore.getInstance();
        partiesRepo = firestore.collection("parties");

        setTag(tagListFood, party.getFood(), viewBinding.foodChipGroup);
        setTag(tagListDrinks, party.getDrinks(), viewBinding.drinksChipGroup);

        viewBinding.textPartyName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setName(viewBinding.textPartyName.getText().toString());
            }
        });
        viewBinding.textPartyDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setDescription(viewBinding.textPartyDescription.getText().toString());
            }
        });
        viewBinding.textDatePicked.setOnClickListener(this);
        viewBinding.textTimePicked.setOnClickListener(this);
        viewBinding.textLocationPicked.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setLocation(viewBinding.textLocationPicked.getText().toString());
            }
        });
        viewBinding.textInputTheme.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setTheme(viewBinding.textInputTheme.getText().toString());
            }
        });
        viewBinding.textInputDressCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setDressCode(viewBinding.textInputDressCode.getText().toString());
            }
        });
        viewBinding.foodChipButton.setOnClickListener(this);
        viewBinding.drinksChipButton.setOnClickListener(this);
        viewBinding.textInputFee.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    parseFee();
                }
            }
        });
        viewBinding.checkBoxPartner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                party.setAllowsPartner(isChecked);
            }
        });

        viewBinding.checkBoxFriends.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                party.setAllowsFriends(isChecked);
            }
        });
        viewBinding.checkBoxChildren.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                party.setAllowsChildren(isChecked);
            }
        });
        viewBinding.checkBoxPets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                party.setAllowsPets(isChecked);
            }
        });
        viewBinding.textInputParking.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setParkingDetails(viewBinding.textInputParking.getText().toString());
            }
        });
        viewBinding.textInputInfo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                party.setAdditionalInformation(viewBinding.textInputInfo.getText().toString());
            }
        });
        viewBinding.submitButton.setOnClickListener(this);

        viewBinding.switchDressCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    viewBinding.dressCodeInputLayout.setVisibility(View.VISIBLE);
                    viewBinding.textInputDressCode.setVisibility(View.VISIBLE);
                    party.setDressCode(viewBinding.textInputDressCode.getText().toString());
                } else {
                    // If the switch button is off
                    viewBinding.dressCodeInputLayout.setVisibility(View.GONE);
                    viewBinding.textInputDressCode.setVisibility(View.GONE);
                    party.setDressCode(null);
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
                    party.setTheme(viewBinding.textInputTheme.getText().toString());
                } else {
                    // If the switch button is off
                    viewBinding.themeInputLayout.setVisibility(View.GONE);
                    viewBinding.textInputTheme.setVisibility(View.GONE);
                    party.setTheme(null);
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
                    for (int i = 0; i < viewBinding.foodChipGroup.getChildCount(); i++) {
                        Chip chip = (Chip) viewBinding.foodChipGroup.getChildAt(i);
                        if (chip.isChecked()) {
                            party.getFood().add(chip.getText().toString());
                        }
                    }
                } else {
                    // If the switch button is off
                    viewBinding.foodChipGroup.setVisibility(View.GONE);
                    viewBinding.foodInputLayout.setVisibility(View.GONE);
                    viewBinding.textInputFood.setVisibility(View.GONE);
                    viewBinding.foodChipButton.setVisibility(View.GONE);
                    viewBinding.foodChipInputLayout.setVisibility(View.GONE);
                    party.getFood().clear();
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
                    for (int i = 0; i < viewBinding.drinksChipGroup.getChildCount(); i++) {
                        Chip chip = (Chip) viewBinding.drinksChipGroup.getChildAt(i);
                        if (chip.isChecked()) {
                            party.getDrinks().add(chip.getText().toString());
                        }
                    }
                } else {
                    // If the switch button is off
                    viewBinding.drinksChipGroup.setVisibility(View.GONE);
                    viewBinding.drinksInputLayout.setVisibility(View.GONE);
                    viewBinding.textInputDrinks.setVisibility(View.GONE);
                    viewBinding.drinksChipButton.setVisibility(View.GONE);
                    viewBinding.drinksChipInputLayout.setVisibility(View.GONE);
                    party.getDrinks().clear();
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
                    parseFee();
                } else {
                    // If the switch button is off
                    viewBinding.feeLinearLayout.setVisibility(View.GONE);
                    viewBinding.feeInputLayout.setVisibility(View.GONE);
                    viewBinding.textInputFee.setVisibility(View.GONE);
                    party.setFee(null);
                }
            }

        });

        viewBinding.switchParking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    viewBinding.parkingInputLayout.setVisibility(View.VISIBLE);
                    viewBinding.textInputParking.setVisibility(View.VISIBLE);
                    party.setParkingDetails(buttonView.getText().toString());
                } else {
                    // If the switch button is off
                    viewBinding.parkingInputLayout.setVisibility(View.GONE);
                    viewBinding.textInputParking.setVisibility(View.GONE);
                    party.setParkingDetails(null);
                }
            }

        });

    }

    private void parseFee() {
        String amountString = viewBinding.textInputFee.getText().toString();
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

        if (v == viewBinding.textDatePicked) {
            // Get Current Date
            final Calendar currentDate = Calendar.getInstance();
            int currentYear = currentDate.get(Calendar.YEAR);
            int currentMonth = currentDate.get(Calendar.MONTH);
            int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar date = Calendar.getInstance();
                    if (party.getTimestamp() != null) {
                        date.setTimeInMillis(party.getTimestamp());
                    }
                    date.set(year, monthOfYear, dayOfMonth);
                    party.setTimestamp(date.getTimeInMillis());

                    String dateText = SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(date.getTime());
                    viewBinding.textDatePicked.setText(dateText);
//                    viewBinding.textDatePicked.setText(dayOfMonth + "-" + ((monthOfYear + 1) <= 9 ? "0" + (monthOfYear + 1) : String.valueOf((monthOfYear + 1))) + "-" + year);
                }
            }, currentYear, currentMonth, currentDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); //sets today's date as minimum date -> all the past dates are disabled
            datePickerDialog.show();
        } else if (v == viewBinding.textTimePicked) {
            // Get Current Time
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);


            // Launch Time Picker Dialog
            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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
                    viewBinding.textTimePicked.setText(timeText);
                }
            }, hour, minute, android.text.format.DateFormat.is24HourFormat(getApplicationContext())).show();
        } else if (v == viewBinding.foodChipButton) {
            String foodEntered = viewBinding.textInputFood.getText().toString();
            if (!foodEntered.trim().isEmpty() && !chipExists(foodEntered, viewBinding.foodChipGroup)) {
                Chip newChip = new Chip(CreatePartyActivity.this);
                ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Choice);  //change to CustomChip style in the future
                newChip.setChipDrawable(drawable);
                newChip.setText(foodEntered);
                party.getFood().add(foodEntered);
                newChip.setChecked(true);
                viewBinding.foodChipGroup.addView(newChip);
            }
            viewBinding.textInputFood.setText("");
        } else if (v == viewBinding.drinksChipButton) {
            String drinkEntered = viewBinding.textInputDrinks.getText().toString();
            if (!drinkEntered.trim().isEmpty() && !chipExists(drinkEntered, viewBinding.drinksChipGroup)) {
                Chip newChip = new Chip(CreatePartyActivity.this);
                ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Choice);  //change to CustomChip style in the future
                newChip.setChipDrawable(drawable);
                newChip.setText(drinkEntered);
                party.getDrinks().add(drinkEntered);
                newChip.setChecked(true);
                viewBinding.drinksChipGroup.addView(newChip);
            }
            viewBinding.textInputDrinks.setText("");
        } else if (v == viewBinding.submitButton) {
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
                            Log.e(TAG,"Persisting party failed");
                            Snackbar.make(viewBinding.getRoot(), "Saving failed", Snackbar.LENGTH_SHORT).show();
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
