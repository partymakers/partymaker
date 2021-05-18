package club.partymaker.partymaker.party;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import club.partymaker.partymaker.R;
import club.partymaker.partymaker.databinding.ActivityCreatePartyBinding;

public class CreatePartyActivity extends AppCompatActivity {
    static final private String TAG = CreatePartyActivity.class.getSimpleName();
    static private final Collection<String> defaultFood = new TreeSet<>(Arrays.asList("Polish", "Pizza", "Kebab", "Sushi", "Asian", "Italian", "Burgers", "Mexican", "Vietnamese"));
    static private final Collection<String> defaultDrinks = new TreeSet<>(Arrays.asList("Light beer", "Craft beer", "Cocktails", "Juice", "Soft drinks", "Vodka", "Whiskey", "Shots", "Wine", "Tea", "Coffee"));

    private ActivityCreatePartyBinding dataBinding;
    private PartyEntity party;

    // TODO: text input check and set errors https://codelabs.developers.google.com/codelabs/mdc-111-kotlin/#2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_party);
        setContentView(dataBinding.getRoot());
        if (getIntent().hasExtra("party")) {
            party = getIntent().getParcelableExtra("party");
        } else {
            party = new PartyEntity();
            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser(), "Firebase user is null").getUid();
            party.getOrganizersIds().add(userId);
            party.getParticipantsIds().add(userId);
        }
        dataBinding.setParty(party);

        setupChips(dataBinding.foodChipGroup, defaultFood, party.getFood());
        setupChips(dataBinding.drinksChipGroup, defaultDrinks, party.getDrinks());
//        It's impossible to set focus listeners in the layout file
        dataBinding.textDatePicked.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) onDateTime(v);
        });
        dataBinding.textTimePicked.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) onDateTime(v);
        });
    }

    public void onDateTime(View view) {
        if (view == dataBinding.textDatePicked) {
            handleDate();
        } else if (view == dataBinding.textTimePicked) {
            handleTime();
        }
    }

    public static String timestampToDateString(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(new Date(timestamp));
    }

    public static String timestampToTimeString(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(timestamp));
    }

    public void onAddChip(View view) {
        Editable input = null;
        if (view == dataBinding.foodChipButton) {
            input = dataBinding.textInputFood.getText();
        } else if (view == dataBinding.drinksChipButton) {
            input = dataBinding.textInputDrinks.getText();
        }
        if (input == null || input.length() == 0) {
            return;
        }
        String name = input.toString().trim();

        if (view == dataBinding.foodChipButton && isChipNameUnique(defaultFood, party.getFood(), name)) {
            party.getFood().add(name);
            addChipView(dataBinding.foodChipGroup, party.getFood(), name);
        } else if (view == dataBinding.drinksChipButton && isChipNameUnique(defaultDrinks, party.getDrinks(), name)) {
            party.getDrinks().add(name);
            addChipView(dataBinding.drinksChipGroup, party.getDrinks(), name);
        }
    }

    public void onSubmit(View view) {
        if (inputIsValid()) {
            DocumentReference firestoreDocument;
            if (party.getId() == null || party.getId().isEmpty()) {
                firestoreDocument = FirebaseFirestore.getInstance().collection("parties").document();
            } else {
                firestoreDocument = FirebaseFirestore.getInstance().collection("parties").document(party.getId());
            }
            firestoreDocument.set(party)
                    .addOnSuccessListener(aVoid -> {
                        Log.i(TAG, "Party persisted.");
                        Toast.makeText(CreatePartyActivity.this.getApplicationContext(), "Party created!", Toast.LENGTH_SHORT).show();
                        CreatePartyActivity.this.finish();
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                        Log.e(TAG, "Persisting party failed");
                        Snackbar.make(dataBinding.getRoot(), "Saving failed", Snackbar.LENGTH_SHORT).show();
                    });
        }
    }

    private void handleDate() {
        // Get current date or set one chosen previously
        final Calendar calendar = Calendar.getInstance();
        if (party.getTimestamp() != null) {
            calendar.setTimeInMillis(party.getTimestamp());
        }
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, R.style.DialogTheme,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    party.setTimestamp(calendar.getTimeInMillis());
                },
                currentYear, currentMonth, currentDay
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
        dataBinding.textDatePicked.setError(null);
    }

    private void handleTime() {
        // Get current time or set one chosen previously
        final Calendar calendar = Calendar.getInstance();
        if (party.getTimestamp() != null) {
            calendar.setTimeInMillis(party.getTimestamp());
        }
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(
                this, R.style.DialogTheme,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    party.setTimestamp(calendar.getTimeInMillis());
                },
                currentHour, currentMinute, android.text.format.DateFormat.is24HourFormat(getApplicationContext())
        ).show();
    }

    private void setupChips(ChipGroup chipGroup, Collection<String> defaultNames, List<String> selectedNames) {
        Set<String> allChips = new LinkedHashSet<>(defaultNames);
        allChips.addAll(selectedNames);
        for (String chipText : allChips) {
            addChipView(chipGroup, selectedNames, chipText);
        }
    }

    private void addChipView(ChipGroup chipGroup, List<String> selectedNames, String name) {
        Chip chip = new Chip(this);

        ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.CustomChipStyle);  //change to CustomChip style in the future
        chip.setChipDrawable(drawable);
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setText(name);

        if (selectedNames.contains(name)) {
            chip.setChecked(true);
            chip.setOnLongClickListener(v -> {
                selectedNames.remove(name);
                chipGroup.removeView(chip);
                return true;
            });
        }
        chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedNames.add(buttonView.getText().toString());
            } else {
                selectedNames.remove(buttonView.getText().toString());
            }
        });
        chipGroup.addView(chip);
    }

    private boolean isChipNameUnique(Collection<String> defaultChips, Collection<String> selectedChips, String name) {
        HashSet<String> allChips = new HashSet<>(defaultChips);
        allChips.addAll(selectedChips);
        return !allChips.contains(name);
    }

    private boolean inputIsValid() {
        validateName();
        validateDate();
        if (dataBinding.textPartyName.getError() != null || dataBinding.textDatePicked.getError() != null) {
            return false;
        }

        return true;
    }

    private void validateName() {
        if (dataBinding.textPartyName.getText().toString().trim().isEmpty()) {
            dataBinding.textPartyName.setError("Name can't be empty");
            dataBinding.CreatePartyScrollView.smoothScrollTo(0, 0);
        } else {
            dataBinding.textPartyName.setError(null);
        }
    }

    private void validateDate() {
        if (dataBinding.textDatePicked.getText().toString().trim().isEmpty()) {
            dataBinding.textDatePicked.setError("Choose date");
            dataBinding.CreatePartyScrollView.smoothScrollTo(0, 0);
        } else {
            dataBinding.textDatePicked.setError(null);
        }
    }
}
