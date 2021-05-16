package club.partymaker.partymaker.dashboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import club.partymaker.partymaker.R;
import club.partymaker.partymaker.databinding.FragmentDashboardBinding;
import club.partymaker.partymaker.party.CreatePartyActivity;
import club.partymaker.partymaker.party.ViewPartyActivity;
import club.partymaker.partymaker.user.UserViewModel;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding dataBinding;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        dataBinding.setLifecycleOwner(requireActivity());
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        dataBinding.setViewmodel(userViewModel);
        dataBinding.setFragment(this);

        return dataBinding.getRoot();
    }

    public void onJoin(View view) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_invite_code, null);
        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setTitle("Enter invite code")
                .setPositiveButton("Ok", (dialog, which) -> {
                    EditText editText = dialogView.findViewById(R.id.inviteCode);
                    if (editText.getText().length() > 0) {
                        Intent intent = new Intent(getActivity(), ViewPartyActivity.class);
                        intent.putExtra("partyCode", editText.getText().toString());
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }

    public void onOrganize(View view) {
        Intent intent = new Intent(getActivity(), CreatePartyActivity.class);
        startActivity(intent);
    }
}
