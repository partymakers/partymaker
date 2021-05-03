package com.github.partymakers.partymaker.party;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.github.partymakers.partymaker.R;
import com.github.partymakers.partymaker.databinding.ActivityViewPartyBinding;

public class ViewPartyActivity extends AppCompatActivity {
    private ActivityViewPartyBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_party);
        setContentView(dataBinding.getRoot());

    }
}
