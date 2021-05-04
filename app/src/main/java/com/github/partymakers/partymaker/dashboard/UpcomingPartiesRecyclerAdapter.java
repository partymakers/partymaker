package com.github.partymakers.partymaker.dashboard;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.partymakers.partymaker.R;
import com.github.partymakers.partymaker.party.PartyEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.github.partymakers.partymaker.databinding.PartyItemBinding;
import com.github.partymakers.partymaker.party.ViewPartyActivity;

import static com.github.partymakers.partymaker.BR.party;
import static com.github.partymakers.partymaker.BR.dateTime;

public class UpcomingPartiesRecyclerAdapter extends RecyclerView.Adapter<UpcomingPartiesRecyclerAdapter.ViewHolder> implements UpcomingPartiesCardClickListener {
    private List<PartyEntity> parties;
    private Context thiscontext;
    private View view;

    public UpcomingPartiesRecyclerAdapter(List<PartyEntity> myDataset) {
        this.parties = myDataset;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public PartyItemBinding partyItemBinding;

        public ViewHolder(PartyItemBinding binding) {
            super(binding.getRoot());
            view = binding.getRoot();
            this.partyItemBinding = binding;
        }

        public void bind(PartyEntity partyEntity, String date) {
            partyItemBinding.setVariable(party, partyEntity);
            partyItemBinding.setVariable(dateTime, date);
            partyItemBinding.executePendingBindings();
        }
    }

    @Override
    public UpcomingPartiesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PartyItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.party_item, parent, false);
        thiscontext = parent.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        PartyEntity party = parties.get(position);
        String dateTime = SimpleDateFormat.getDateTimeInstance(DateFormat.FULL, 2).format(new Date(party.getTimestamp()));
        holder.bind(party, dateTime);
        holder.partyItemBinding.setItemClickListener(this);
    }

    @Override
    public int getItemCount() {
        return parties.size();
    }

    public void onCardClicked(PartyEntity partyEntity) {
        Intent intent = new Intent(view.getContext(), ViewPartyActivity.class);
        intent.putExtra("partyCode", partyEntity.getId());
        thiscontext.startActivity(intent);
    }

}
