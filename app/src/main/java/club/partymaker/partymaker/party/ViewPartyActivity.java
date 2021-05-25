package club.partymaker.partymaker.party;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import club.partymaker.partymaker.R;
import club.partymaker.partymaker.databinding.ActivityViewPartyBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ViewPartyActivity extends AppCompatActivity {
    private ViewPartyViewModel viewModel;
    private ActivityViewPartyBinding dataBinding;

    private final ActivityResultLauncher<Intent> AuthUiLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        viewModel.setPartyId(viewModel.getPartyIdValue());
//        TODO: handle AuthUi results
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_party);
        setContentView(dataBinding.getRoot());
        dataBinding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(ViewPartyViewModel.class);
        dataBinding.setViewmodel(viewModel);

        if (getIntent().hasExtra("partyCode")) {
            viewModel.setPartyId(getIntent().getStringExtra("partyCode"));
        } else {
            FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                    .addOnSuccessListener(this, pendingData -> {
                        if (pendingData != null && pendingData.getLink() != null) {
                            String id = pendingData.getLink().getLastPathSegment();
                            if (!viewModel.isAuthenticated()) {
                                AuthUiLauncher.launch(viewModel.getAuthUiIntent());
                            }
                            viewModel.setPartyId(id);
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
                .putExtra(Intent.EXTRA_TEXT, String.format("%s:\n%s", viewModel.getPartyNameValue(), viewModel.createDynamicLink()))
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
