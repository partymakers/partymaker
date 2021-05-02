package com.github.partymakers.partymaker.party;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.github.partymakers.partymaker.R;
import com.github.partymakers.partymaker.databinding.ActivityJoinPartyBinding;

public class JoinPartyActivity extends AppCompatActivity {
private ActivityJoinPartyBinding dataBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding= DataBindingUtil.setContentView(this, R.layout.activity_join_party);
        setContentView(dataBinding.getRoot());

// replace with proper data binding
        dataBinding.switchJoinFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on
                    dataBinding.foodJoinInputLayout.setVisibility(View.VISIBLE);
                    dataBinding.textInputJoinFood.setVisibility(View.VISIBLE);
                } else {
                    // If the switch button is off
                    dataBinding.foodJoinInputLayout.setVisibility(View.GONE);
                    dataBinding.textInputJoinFood.setVisibility(View.GONE);
                }
            }

        });
    }
}
