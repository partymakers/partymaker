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

        viewModel = new ViewModelProvider(this, new ViewPartyViewModel.Factory(partyCode))
                .get(ViewPartyViewModel.class);

        dataBinding.setViewmodel(viewModel);
    }

    public void onShare(View view) {
        Intent sendIntent = new Intent()
                .setAction(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, viewModel.getPartyId())
                .setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent,null);
        startActivity(shareIntent);
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
