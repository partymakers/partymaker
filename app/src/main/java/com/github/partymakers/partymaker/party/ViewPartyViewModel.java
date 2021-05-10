package com.github.partymakers.partymaker.party;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewPartyViewModel extends ViewModel {
    private DocumentReference documentReference;

    private final MutableLiveData<PartyEntity> party = new MutableLiveData<>();
    private final LiveData<String> formattedDateTime = Transformations.map(party,
            partyEntity -> SimpleDateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(new Date(partyEntity.getTimestamp())));
    private final LiveData<Boolean> editable = Transformations.map(party,
            partyEntity -> partyEntity.getOrganizersIds().contains(FirebaseAuth.getInstance().getUid()));
    private final LiveData<Boolean> isParticipant = Transformations.map(party,
            partyEntity -> partyEntity.getParticipantsIds().contains(FirebaseAuth.getInstance().getUid()));
    private final LiveData<Boolean> isOrganiser = Transformations.map(party,
            partyEntity -> partyEntity.getOrganizersIds().contains(FirebaseAuth.getInstance().getUid()));

    public void setPartyId(String partyId) {
        if (party.getValue() != null && party.getValue().getId().equals(partyId)) {
            return;
        }
        documentReference = FirebaseFirestore.getInstance().collection("parties").document(partyId);
        documentReference.addSnapshotListener((value, error) -> {
            if (value != null) {
                party.setValue(value.toObject(PartyEntity.class));
            } else if (error != null) {
                error.printStackTrace();
            }
        });
    }

    public LiveData<PartyEntity> getParty() {
        return party;
    }

    public LiveData<String> getFormattedDateTime() {
        return formattedDateTime;
    }

    public LiveData<Boolean> getEditable() {
        return editable;
    }

    public LiveData<Boolean> getIsParticipant() {
        return isParticipant;
    }

    public LiveData<Boolean> getIsOrganiser() {
        return isOrganiser;
    }

    public void acceptInvitation() {
        if (party.getValue() == null) {
            return;
        }
        List<String> participantsIds = party.getValue().getParticipantsIds();
            String userId = FirebaseAuth.getInstance().getUid();
        if (!participantsIds.contains(userId)) {
            participantsIds.add(userId);
        }
        documentReference.update("participantsIds", participantsIds);
    }

//    return true if user was deleted from participants
    public boolean rejectInvitation() {
        List<String> participantsIds = party.getValue().getParticipantsIds();
        boolean removed = participantsIds.remove(FirebaseAuth.getInstance().getUid());
        if (removed) {
            documentReference.update("participantsIds", participantsIds);
        }
        return removed;
    }
}
