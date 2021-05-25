package club.partymaker.partymaker.party;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import club.partymaker.partymaker.user.UserRepository;

@Singleton
public class PartyRepository {
    private static final String TAG = PartyRepository.class.getSimpleName();

    private final CollectionReference firebaseCollection = FirebaseFirestore.getInstance()
            .collection("parties");
    private final Map<String, PartyEntity> cacheData = new LinkedHashMap<>();
    private final MutableLiveData<Map<String, PartyEntity>> cache = new MutableLiveData<>();
    private final LiveData<List<PartyEntity>> parties;

    @Inject
    public PartyRepository(UserRepository userRepository) {
        AtomicReference<ListenerRegistration> atomicRegistration = new AtomicReference<>();
        userRepository.getUserId().observeForever(id -> {
            if (id != null && atomicRegistration.get() == null) {
                ListenerRegistration registration = firebaseCollection
                        .whereArrayContains("participantsIds", id)
                        .orderBy("timestamp", Query.Direction.ASCENDING)
                        .addSnapshotListener((value, error) -> {
                            if (value != null) {
                                for (DocumentChange change : value.getDocumentChanges()) {
                                    PartyEntity changedParty = change.getDocument().toObject(PartyEntity.class);
                                    if (change.getType() == DocumentChange.Type.ADDED || change.getType() == DocumentChange.Type.MODIFIED) {
                                        cacheData.put(changedParty.getId(), changedParty);
                                    } else if (change.getType() == DocumentChange.Type.REMOVED) {
                                        cacheData.remove(changedParty.getId());
                                    }
                                    cache.setValue(cacheData);
                                }
                            } else if (error != null) {
                                FirebaseFirestoreException.Code errorCode = error.getCode();
                                Log.e(TAG, String.format("Error while downloading parties. (%d: %s)", errorCode.value(), errorCode.toString()));
                            }
                        });
                atomicRegistration.set(registration);
            } else if (id == null && atomicRegistration.get() != null) {
                atomicRegistration.get().remove();
            }
        });

        parties = Transformations.map(cache, input -> new ArrayList<>(input.values()));
    }

    public LiveData<PartyEntity> findPartyById(String partyId) {
        if (!cacheData.containsKey(partyId)) {
            firebaseCollection.document(partyId)
                    .addSnapshotListener((value, error) -> {
                        if (value != null) {
                            PartyEntity party = value.toObject(PartyEntity.class);
                            cacheData.put(partyId, party);
                            cache.setValue(cacheData);
                        } else if (error != null) {
                            FirebaseFirestoreException.Code errorCode = error.getCode();
                            Log.e(TAG, String.format("Error while downloading party with id = %s. (%d: %s)", partyId, errorCode.value(), errorCode.toString()));
                        }
                    });
        }
        return Transformations.map(cache, input -> input.get(partyId));
    }

    public LiveData<List<PartyEntity>> findPartiesBefore(long timestampInMillis) {
        return Transformations.map(parties, input -> input.stream()
                .filter(partyEntity -> partyEntity.getTimestamp() < timestampInMillis)
                .collect(Collectors.toList()));
    }

    public LiveData<List<PartyEntity>> findPartiesAfter(long timestampInMillis) {
        return Transformations.map(parties, input -> input.stream()
                .filter(partyEntity -> partyEntity.getTimestamp() > timestampInMillis)
                .collect(Collectors.toList()));
    }

    public LiveData<List<PartyEntity>> findPartiesWhere(Predicate<PartyEntity> predicate) {
        return Transformations.map(parties, input -> input.stream()
                .filter(predicate)
                .collect(Collectors.toList()));
    }

    public LiveData<List<PartyEntity>> getParties() {
        return parties;
    }

    public void persistParty(PartyEntity party, OnCompleteListener<Void> onCompleteListener) {
        DocumentReference reference;
        if (party.getId() == null || party.getId().isEmpty()) {
            reference = firebaseCollection.document();
        } else {
            reference = firebaseCollection.document(party.getId());
        }
        Task<Void> setTask = reference.set(party);
        if (onCompleteListener != null) {
            setTask.addOnCompleteListener(onCompleteListener);
        }
    }

    public void persistParty(PartyEntity party) {
        persistParty(party, null);
    }

    public void updateParticipantsIds(String partyId, List<String> participantsIds, OnCompleteListener<Void> onCompleteListener) {
        Task<Void> updateTask = firebaseCollection.document(partyId).update("participantsIds", participantsIds);
        if (onCompleteListener != null) {
            updateTask.addOnCompleteListener(onCompleteListener);
        }
    }

    public void updateParticipantsIds(String partyId, List<String> participantsIds) {
        updateParticipantsIds(partyId, participantsIds, null);
    }
}
