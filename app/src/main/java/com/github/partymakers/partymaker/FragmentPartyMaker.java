package com.github.partymakers.partymaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;

import com.github.partymakers.partymaker.databinding.FragmentPartymakerBinding;


public class FragmentPartyMaker extends Fragment {
    private FragmentPartymakerBinding dataBinding;

    // TODO: data binding for the fragment(?)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_partymaker, container, false);
    }

}
