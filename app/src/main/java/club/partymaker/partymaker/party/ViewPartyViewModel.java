package club.partymaker.partymaker.party;

import android.content.Intent;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import club.partymaker.partymaker.user.UserRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ViewPartyViewModel extends ViewModel {
    private static final String TAG = ViewPartyViewModel.class.getSimpleName();

    private final MutableLiveData<String> partyId = new MutableLiveData<>();
    private final LiveData<PartyEntity> party;
    private final LiveData<String> formattedDateTime;
    private final LiveData<Boolean> isOrganiser;
    private final LiveData<Boolean> isParticipant;

    private final UserRepository userRepository;
    private final PartyRepository partyRepository;

    @Inject
    public ViewPartyViewModel(UserRepository userRepository, PartyRepository partyRepository) {
        this.userRepository = userRepository;
        this.partyRepository = partyRepository;

        party = Transformations.switchMap(partyId, partyRepository::findPartyById);
        formattedDateTime = Transformations.map(party, partyEntity -> {
                    if (partyEntity == null) {
                        return null;
                    }
                    return SimpleDateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(new Date(partyEntity.getTimestamp()));
                });
        isOrganiser = Transformations.map(party, partyEntity -> {
                    if (partyEntity == null) {
                        return null;
                    }
                    return partyEntity.getOrganizersIds().contains(FirebaseAuth.getInstance().getUid());
                });
        isParticipant = Transformations.map(party, partyEntity -> {
                    if (partyEntity == null) {
                        return null;
                    }
                    return partyEntity.getParticipantsIds().contains(FirebaseAuth.getInstance().getUid());
                });
    }

    public void setPartyId(String partyId) {
        this.partyId.setValue(partyId);
    }

    public void acceptInvitation() {
        throwIfPartyIdIsNotSet();
        List<String> participantsIds = getPartyValue().getParticipantsIds();
        String userId = userRepository.getUserIdValue();
        if (!participantsIds.contains(userId)) {
            participantsIds.add(userId);
        }
        partyRepository.updateParticipantsIds(getPartyIdValue(), participantsIds);
    }

    /**
     * @return true if user was deleted from participants, false otherwise
     */
    public boolean rejectInvitation() {
        throwIfPartyIdIsNotSet();
        List<String> participantsIds = getPartyValue().getParticipantsIds();
        boolean isRemoved = participantsIds.remove(userRepository.getUserIdValue());
        if (isRemoved) {
            partyRepository.updateParticipantsIds(getPartyIdValue(), participantsIds);
        }
        return isRemoved;
    }

    public Uri createDynamicLink() {
        throwIfPartyIdIsNotSet();
        return FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setDomainUriPrefix("https://partymaker.club/links/")
                .setLink(Uri.parse("https://partymaker.club/event/" + getPartyIdValue()))
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(getPartyNameValue())
                                .setDescription(getPartyDescriptionValue())
                                .build())
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder().build())
                .buildDynamicLink()
                .getUri();
    }

    public boolean isAuthenticated() {
        return userRepository.getUserIdValue() != null;
    }

    public Intent getAuthUiIntent() {
        return userRepository.getAuthUiIntent();
    }

    public String getPartyIdValue() {
        return partyId.getValue();
    }

    public String getPartyNameValue() {
        return getPartyValue().getName();
    }

    public String getPartyDescriptionValue() {
        return getPartyValue().getDescription();
    }

    public LiveData<PartyEntity> getParty() {
        return party;
    }

    public LiveData<String> getFormattedDateTime() {
        return formattedDateTime;
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
    private PartyEntity getPartyValue() {
        return Objects.requireNonNull(party.getValue(), "Party is null");
    }
}
