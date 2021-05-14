package com.github.partymakers.partymaker.party;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.github.partymakers.partymaker.R;
import com.github.partymakers.partymaker.databinding.ActivityViewPartyBinding;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

public class ViewPartyActivity extends AppCompatActivity {
    private ViewPartyViewModel viewModel;
    private ActivityViewPartyBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_party);
        setContentView(dataBinding.getRoot());
        dataBinding.setLifecycleOwner(this);

        MutableLiveData<String> partyId = new MutableLiveData<>();
        partyId.observe(this, id -> {
            viewModel = new ViewModelProvider(ViewPartyActivity.this, new ViewPartyViewModel.Factory(id)).get(ViewPartyViewModel.class);
            dataBinding.setViewmodel(viewModel);
        });

        if (getIntent().hasExtra("partyCode")) {
            partyId.setValue(getIntent().getStringExtra("partyCode"));
        } else {
            FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                    .addOnSuccessListener(this, pendingData -> {
                        if (pendingData != null && pendingData.getLink() != null) {
                            String id = pendingData.getLink().getLastPathSegment();
                            partyId.setValue(id);
                        } else {
                            finish();
                        }
                    })
                    .addOnFailureListener(this, e -> finish());
        }
    }

    public void onShare(View view) {
        Intent sendIntent = new Intent()
                .setAction(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, String.format("%s:\n%s", viewModel.getPartyName(), viewModel.getDynamicLink()))
                .setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
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
