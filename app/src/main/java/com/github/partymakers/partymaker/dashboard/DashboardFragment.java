package com.github.partymakers.partymaker.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.partymakers.partymaker.R;
import com.github.partymakers.partymaker.databinding.FragmentDashboardBinding;
import com.github.partymakers.partymaker.party.CreatePartyActivity;
import com.github.partymakers.partymaker.party.ViewPartyActivity;
import com.github.partymakers.partymaker.user.UserViewModel;

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
      
        Button join = dataBinding.buttonJoin;
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ViewPartyActivity.class);
                startActivity(intent);
            }
        });

        return dataBinding.getRoot();
    }

    public void onOrganize(View view) {
        Intent intent = new Intent(getActivity(), CreatePartyActivity.class);
        startActivity(intent);
    }
}
