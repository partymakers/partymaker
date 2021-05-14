package com.github.partymakers.partymaker.party;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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

    public static class Factory implements ViewModelProvider.Factory {
        private final String partyId;

        public Factory(String partyId) {
            this.partyId = partyId;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ViewPartyViewModel(partyId);
        }
    }

    private final DocumentReference documentReference;

    private final MutableLiveData<PartyEntity> party = new MutableLiveData<>();
    private final LiveData<String> formattedDateTime;
    private final LiveData<Boolean> editable;
    private final LiveData<Boolean> isParticipant;
    private final LiveData<Boolean> isOrganiser;

    public ViewPartyViewModel(String partyId) {
        formattedDateTime = Transformations.map(party,
                partyEntity -> SimpleDateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(new Date(partyEntity.getTimestamp())));
        editable = Transformations.map(party,
                partyEntity -> partyEntity.getOrganizersIds().contains(FirebaseAuth.getInstance().getUid()));
        isParticipant = Transformations.map(party,
                partyEntity -> partyEntity.getParticipantsIds().contains(FirebaseAuth.getInstance().getUid()));
        isOrganiser = Transformations.map(party,
                partyEntity -> partyEntity.getOrganizersIds().contains(FirebaseAuth.getInstance().getUid()));

        documentReference = FirebaseFirestore.getInstance().collection("parties").document(partyId);
        documentReference.addSnapshotListener((value, error) -> {
            if (value != null) {
                party.setValue(value.toObject(PartyEntity.class));
            } else if (error != null) {
                error.printStackTrace();
            }
        });
    }

    public void acceptInvitation() {
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
        List<String> participantsIds = getPartySafe().getParticipantsIds();
        boolean isRemoved = participantsIds.remove(FirebaseAuth.getInstance().getUid());
        if (isRemoved) {
            documentReference.update("participantsIds", participantsIds);
        }
        return isRemoved;
    }

    public Uri getDynamicLink() {
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

    public LiveData<Boolean> getEditable() {
        return editable;
    }

    public LiveData<Boolean> getIsParticipant() {
        return isParticipant;
    }

    public LiveData<Boolean> getIsOrganiser() {
        return isOrganiser;
    }

    @NonNull
    private PartyEntity getPartySafe() {
        return Objects.requireNonNull(party.getValue(), "Party is null");
    }
}
