package club.partymaker.partymaker.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import club.partymaker.partymaker.R;
import club.partymaker.partymaker.databinding.FragmentUpcomingPartiesBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UpcomingPartiesFragment extends Fragment {
    private FragmentUpcomingPartiesBinding dataBinding;
    private PartyListViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_upcoming_parties, container, false);
        dataBinding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new ViewModelProvider(requireActivity()).get(PartyListViewModel.class);

        RecyclerView recyclerView = dataBinding.recyclerViewUpcomingFragment;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        PartyListRecyclerAdapter recyclerAdapter = new PartyListRecyclerAdapter();
        viewModel.getUpcomingParties().observe(getViewLifecycleOwner(), recyclerAdapter::setParties);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        dataBinding.setMyAdapter(recyclerAdapter);

        return dataBinding.getRoot();
    }

}
