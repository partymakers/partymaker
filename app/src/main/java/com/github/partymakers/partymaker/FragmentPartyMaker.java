package com.github.partymakers.partymaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.github.partymakers.partymaker.databinding.FragmentPartymakerBinding;
import com.github.partymakers.partymaker.party.CreatePartyActivity;


public class FragmentPartyMaker extends Fragment {
    private FragmentPartymakerBinding dataBinding;

    // TODO: data binding for the fragment(?)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_partymaker, container, false);
        View view = dataBinding.getRoot();
        //set data


        Button organize = dataBinding.buttonOrganize;
        organize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreatePartyActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
