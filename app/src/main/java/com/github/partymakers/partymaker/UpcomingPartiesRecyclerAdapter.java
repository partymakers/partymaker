package com.github.partymakers.partymaker;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.partymakers.partymaker.party.PartyEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.partymakers.partymaker.databinding.PartyItemBinding;

import static com.github.partymakers.partymaker.BR.party;
import static com.github.partymakers.partymaker.BR.dateTime;

public class UpcomingPartiesRecyclerAdapter extends RecyclerView.Adapter<UpcomingPartiesRecyclerAdapter.ViewHolder> {
    private List<PartyEntity> parties = new ArrayList<>();

    public UpcomingPartiesRecyclerAdapter(List<PartyEntity> myDataset) {
        this.parties = myDataset;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public PartyItemBinding partyItemBinding;

        public ViewHolder(PartyItemBinding binding) {
            super(binding.getRoot());
            this.partyItemBinding = binding;
        }

        public void bind(Object obj, String date) {
            partyItemBinding.setVariable(party, obj);
            partyItemBinding.setVariable(dateTime, date);
            partyItemBinding.executePendingBindings();
        }
    }

    @Override
    public UpcomingPartiesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PartyItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.party_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        PartyEntity party = parties.get(position);
        String dateTime = SimpleDateFormat.getDateTimeInstance(DateFormat.FULL, 2).format(new Date(party.getTimestamp()));
        holder.bind(party, dateTime);
    }

    @Override
    public int getItemCount() {
        return parties.size();
    }

}
