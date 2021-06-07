package club.partymaker.partymaker.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import club.partymaker.partymaker.R;
import club.partymaker.partymaker.databinding.ActivityUserEditBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserEditActivity extends AppCompatActivity {
    private UserSettingsViewModel viewModel;
    private ActivityUserEditBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_edit);
        setContentView(dataBinding.getRoot());
        dataBinding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(UserSettingsViewModel.class);
        dataBinding.setViewmodel(viewModel);
    }

    public void onUpdateNickname(View view){
        if (!dataBinding.nickname.getText().toString().trim().isEmpty()){ viewModel.updateDisplayedName(dataBinding.nickname.getText().toString().trim());
            Toast.makeText(UserEditActivity.this.getApplicationContext(), "Nickname updated!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUpdatePassword(View view){
        if (!dataBinding.passwordOld.getText().toString().trim().isEmpty() && !dataBinding.passwordNew.getText().toString().trim().isEmpty()) {viewModel.updatePassword(dataBinding.passwordOld.getText().toString(), dataBinding.passwordNew.getText().toString());}
    }

}
