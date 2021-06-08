package club.partymaker.partymaker.party;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import club.partymaker.partymaker.R;
import club.partymaker.partymaker.databinding.ActivityParticipantsBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ParticipantsActivity extends AppCompatActivity {
    private ParticipantsViewModel viewModel;
    private ActivityParticipantsBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_participants);
        setContentView(dataBinding.getRoot());
        dataBinding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(ParticipantsViewModel.class);
        Intent intent = getIntent();
        if (!intent.hasExtra("partyId")) {
            finish();
        }
        viewModel.setParty(intent.getStringExtra("partyId"));
        dataBinding.setViewmodel(viewModel);
        dataBinding.setRecyclerAdapter(new ParticipantsRecyclerAdapter(this, viewModel.getParticipants()));


    }
}
