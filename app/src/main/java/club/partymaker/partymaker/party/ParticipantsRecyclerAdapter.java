package club.partymaker.partymaker.party;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import club.partymaker.partymaker.R;
import club.partymaker.partymaker.databinding.ItemParticipantBinding;
import club.partymaker.partymaker.user.PublicUserDetailsEntity;

public class ParticipantsRecyclerAdapter extends RecyclerView.Adapter<ParticipantsRecyclerAdapter.ViewHolder> {
    private final AppCompatActivity activity;
    private final LiveData<List<PublicUserDetailsEntity>> participants;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemParticipantBinding dataBinding;

        public ViewHolder(ItemParticipantBinding binding) {
            super(binding.getRoot());
            dataBinding = binding;
        }

        private void bind(PublicUserDetailsEntity details) {
            dataBinding.setParticipantDetails(details);
            dataBinding.participantCard.setOnClickListener(v -> {
//                TODO: display dialog with participant's comment if available
            });
        }
    }

    public ParticipantsRecyclerAdapter(AppCompatActivity activity, LiveData<List<PublicUserDetailsEntity>> participants) {
        this.activity = activity;
        this.participants = participants;
        participants.observe(activity, publicUserDetailsEntities -> {
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemParticipantBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_participant, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(participants.getValue().get(position));
    }

    @Override
    public int getItemCount() {
        if (participants.getValue() != null) {
            return participants.getValue().size();
        }
        return 0;
    }
}
