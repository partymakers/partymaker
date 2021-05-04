package com.github.partymakers.partymaker.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.partymakers.partymaker.party.PartyEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query.Direction;

import java.util.List;

public class UpcomingPartiesViewModel extends ViewModel {
    private final MutableLiveData<List<PartyEntity>> parties = new MutableLiveData<>();
    private final String userId = FirebaseAuth.getInstance().getUid();

    public UpcomingPartiesViewModel() {
        FirebaseFirestore.getInstance().collection("parties")
                .whereArrayContains("participantsIds", userId)
                .orderBy("timestamp", Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        parties.setValue(value.toObjects(PartyEntity.class));
                    } else if (error != null) {
                        error.printStackTrace();
                    }
                });
    }

    public LiveData<List<PartyEntity>> getParties() {
        return parties;
    }
}
