package club.partymaker.partymaker.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import club.partymaker.partymaker.R;
import club.partymaker.partymaker.databinding.ActivityUserSettingsBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserSettingsActivity extends AppCompatActivity {
    private UserSettingsViewModel viewModel;
    private ActivityUserSettingsBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_settings);
        setContentView(dataBinding.getRoot());
        dataBinding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(UserSettingsViewModel.class);
        dataBinding.setViewmodel(viewModel);
    }
}
