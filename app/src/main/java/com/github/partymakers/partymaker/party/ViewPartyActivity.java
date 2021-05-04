package com.github.partymakers.partymaker.party;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.github.partymakers.partymaker.R;
import com.github.partymakers.partymaker.databinding.ActivityViewPartyBinding;

public class ViewPartyActivity extends AppCompatActivity {
    private ViewPartyViewModel viewModel;
    private ActivityViewPartyBinding dataBinding;
    private String partyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        partyCode = getIntent().getStringExtra("partyCode");

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_party);
        setContentView(dataBinding.getRoot());
        dataBinding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(ViewPartyViewModel.class);
        viewModel.setPartyId(partyCode);
//        viewModel.getParty(partyCode).observe(this, partyEntity -> {
//            dataBinding.setParty(partyEntity);
//        });
        dataBinding.setViewmodel(viewModel);
    }

    public void onEdit(View view) {
        Intent intent = new Intent(this, CreatePartyActivity.class);
        intent.putExtra("party", viewModel.getParty().getValue());
        startActivity(intent);
    }

    public void onRespond(View view) {
        if (view == dataBinding.buttonAccept) {
            viewModel.acceptInvitation();
        } else if (view == dataBinding.buttonReject) {
            if (!viewModel.rejectInvitation()) {
                finish();
            }
        }
    }
}
