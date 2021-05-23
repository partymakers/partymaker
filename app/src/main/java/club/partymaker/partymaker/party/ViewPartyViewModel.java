package club.partymaker.partymaker.party;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ViewPartyViewModel extends ViewModel {
    private static final String TAG = ViewPartyViewModel.class.getSimpleName();

    private final MutableLiveData<String> partyId;
    private final LiveData<PartyEntity> party;
    private final LiveData<String> formattedDateTime;
    private final LiveData<Boolean> isEditable;
    private final LiveData<Boolean> isParticipant;
    private final LiveData<Boolean> isOrganiser;

    private DocumentReference documentReference;

    public ViewPartyViewModel() {
        MutableLiveData<PartyEntity> mutableParty = new MutableLiveData<>();
        partyId = new MutableLiveData<>();
        partyId.observeForever(id -> {
            documentReference = FirebaseFirestore.getInstance().collection("parties").document(id);
            documentReference.addSnapshotListener((value, error) -> {
                if (value != null) {
                    mutableParty.setValue(value.toObject(PartyEntity.class));
                } else if (error != null) {
                    error.printStackTrace();
                }
            });
        });
        party = mutableParty;
        formattedDateTime = Transformations.map(party,
                partyEntity -> SimpleDateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(new Date(partyEntity.getTimestamp())));
        isEditable = Transformations.map(party,
                partyEntity -> partyEntity.getOrganizersIds().contains(FirebaseAuth.getInstance().getUid()));
        isParticipant = Transformations.map(party,
                partyEntity -> partyEntity.getParticipantsIds().contains(FirebaseAuth.getInstance().getUid()));
        isOrganiser = Transformations.map(party,
                partyEntity -> partyEntity.getOrganizersIds().contains(FirebaseAuth.getInstance().getUid()));
    }

    public void setPartyId(String partyId) {
        this.partyId.setValue(partyId);
    }

    public void acceptInvitation() {
        throwIfPartyIdIsNotSet();
        List<String> participantsIds = getPartySafe().getParticipantsIds();
        String userId = FirebaseAuth.getInstance().getUid();
        if (!participantsIds.contains(userId)) {
            participantsIds.add(userId);
        }
        documentReference.update("participantsIds", participantsIds);
    }

    /**
     * @return true if user was deleted from participants, false otherwise
     */
    public boolean rejectInvitation() {
        throwIfPartyIdIsNotSet();
        List<String> participantsIds = getPartySafe().getParticipantsIds();
        boolean isRemoved = participantsIds.remove(FirebaseAuth.getInstance().getUid());
        if (isRemoved) {
            documentReference.update("participantsIds", participantsIds);
        }
        return isRemoved;
    }

    public Uri getDynamicLink() {
        throwIfPartyIdIsNotSet();
        return FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setDomainUriPrefix("https://partymaker.club/links/")
                .setLink(Uri.parse("https://partymaker.club/event/" + getPartyId()))
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(getPartyName())
                                .setDescription(getPartyDescription())
                                .build())
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder().build())
                .buildDynamicLink()
                .getUri();
    }

    public String getPartyId() {
        return getPartySafe().getId();
    }

    public String getPartyName() {
        return getPartySafe().getName();
    }

    public String getPartyDescription() {
        return getPartySafe().getDescription();
    }

    public LiveData<PartyEntity> getParty() {
        return party;
    }

    public LiveData<String> getFormattedDateTime() {
        return formattedDateTime;
    }

    public LiveData<Boolean> getIsEditable() {
        return isEditable;
    }

    public LiveData<Boolean> getIsParticipant() {
        return isParticipant;
    }

    public LiveData<Boolean> getIsOrganiser() {
        return isOrganiser;
    }

    private void throwIfPartyIdIsNotSet() {
        String message = "Party id has not been set";
        if (partyId.getValue() == null) {
            Log.e(TAG, message);
            throw new IllegalStateException(message);
        }
    }

    @NonNull
    private PartyEntity getPartySafe() {
        return Objects.requireNonNull(party.getValue(), "Party is null");
    }
}
