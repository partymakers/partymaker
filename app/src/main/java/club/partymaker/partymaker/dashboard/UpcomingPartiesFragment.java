package club.partymaker.partymaker.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import club.partymaker.partymaker.R;
import club.partymaker.partymaker.databinding.FragmentUpcomingPartiesBinding;


public class UpcomingPartiesFragment extends Fragment {
    private FragmentUpcomingPartiesBinding dataBinding;
    private UpcomingPartiesViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_upcoming_parties, container, false);
        dataBinding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new ViewModelProvider(this).get(UpcomingPartiesViewModel.class);

        RecyclerView recyclerView = dataBinding.recyclerViewUpcomingFragment;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        PartyListRecyclerAdapter recyclerAdapter = new PartyListRecyclerAdapter();
        viewModel.getParties().observe(getViewLifecycleOwner(), recyclerAdapter::setParties);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        dataBinding.setMyAdapter(recyclerAdapter);

        return dataBinding.getRoot();
    }

}
