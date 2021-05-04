package com.github.partymakers.partymaker.party;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.github.partymakers.partymaker.R;
import com.github.partymakers.partymaker.databinding.ActivityViewPartyBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class ViewPartyActivity extends AppCompatActivity {
    private ActivityViewPartyBinding dataBinding;

    PartyEntity party = new PartyEntity();

    {
        party.setName("Dope af");
        party.setDescription("Long loooooooooong description");
        party.setTimestamp(new Date().getTime());
        party.setLocation("location");
        party.setTheme("theme");
        party.setDressCode("dress code");
        party.setFood(Arrays.asList("Pizza", "Kebab", "Saszety"));
        party.setDrinks(Arrays.asList("Vodka", "Tea", "void juice"));
        party.setFee("miljon cebulionów");
        party.setAllowsPartner(true);
        party.setParkingDetails("just walk, duh");
        party.setAdditionalInformation("grass is green");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_party);
        setContentView(dataBinding.getRoot());
        dataBinding.setParty(party);

        if (party.getTimestamp() != null) {
            String dateTime = SimpleDateFormat.getDateTimeInstance(DateFormat.FULL, 2).format(new Date(party.getTimestamp()));
            dataBinding.textViewPartyDateTime.setText("When: " + dateTime);
        }
    }
}
