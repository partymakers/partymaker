package club.partymaker.partymaker.dashboard;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import club.partymaker.partymaker.R;
import club.partymaker.partymaker.databinding.PartyItemBinding;
import club.partymaker.partymaker.party.PartyEntity;
import club.partymaker.partymaker.party.ViewPartyActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static club.partymaker.partymaker.BR.dateTime;
import static club.partymaker.partymaker.BR.party;

public class UpcomingPartiesRecyclerAdapter extends RecyclerView.Adapter<UpcomingPartiesRecyclerAdapter.ViewHolder> implements UpcomingPartiesCardClickListener {
    private List<PartyEntity> parties = new ArrayList<>();
    private Context thiscontext;
    private View view;

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

    @NonNull
    @Override
    public UpcomingPartiesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PartyItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.party_item, parent, false);
        thiscontext = parent.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        PartyEntity party = parties.get(position);
        String dateTime = SimpleDateFormat.getDateTimeInstance(DateFormat.FULL, 3).format(new Date(party.getTimestamp()));
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

    public void setParties(List<PartyEntity> parties) {
        this.parties = parties;
        notifyDataSetChanged();
    }
}
