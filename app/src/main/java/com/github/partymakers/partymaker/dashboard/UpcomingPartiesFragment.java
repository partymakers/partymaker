package com.github.partymakers.partymaker.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.partymakers.partymaker.R;
import com.github.partymakers.partymaker.UpcomingPartiesRecyclerAdapter;
import com.github.partymakers.partymaker.databinding.FragmentUpcomingPartiesBinding;
import com.github.partymakers.partymaker.party.PartyEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UpcomingPartiesFragment extends Fragment {
    private FragmentUpcomingPartiesBinding dataBinding;

    private List<PartyEntity> test = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_upcoming_parties, container, false);
        View view = dataBinding.getRoot();

        RecyclerView recyclerView = dataBinding.recyclerViewUpcomingFragment;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        UpcomingPartiesRecyclerAdapter recyclerAdapter = new UpcomingPartiesRecyclerAdapter(test);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        dataBinding.setMyAdapter(recyclerAdapter);

        return view;
    }

}
