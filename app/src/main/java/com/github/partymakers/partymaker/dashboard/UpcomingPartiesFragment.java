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
    PartyEntity p1 = new PartyEntity();
    PartyEntity p2 = new PartyEntity();
    PartyEntity p3 = new PartyEntity();


    {
        p1.setName("Party 1");
        p1.setDescription("Description");
        p1.setTimestamp(new Date().getTime());

        p2.setName("Party 2");
        p2.setDescription("Desc");
        p2.setTimestamp(new Date().getTime());

        p3.setName("Party 3");
        p3.setDescription("MaWhynnbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbn");
        p3.setTimestamp(new Date().getTime());

        test.add(p1);
        test.add(p2);
        test.add(p3);
    }
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
