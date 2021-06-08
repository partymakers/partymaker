package club.partymaker.partymaker.party;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import club.partymaker.partymaker.user.PublicUserDetailsEntity;
import club.partymaker.partymaker.user.UserRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ParticipantsViewModel extends ViewModel {
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private LiveData<PartyEntity> party;
    private LiveData<List<PublicUserDetailsEntity>> participants;

    @Inject
    public ParticipantsViewModel(PartyRepository partyRepository, UserRepository userRepository) {
        this.partyRepository = partyRepository;
        this.userRepository = userRepository;
    }

    public void setParty(String partyId) {
        party = partyRepository.findPartyById(partyId);
        participants = Transformations.switchMap(party, partyEntity -> {
            List<String> ids = partyEntity.getParticipantsIds();
            return userRepository.getPublicUserDetails(ids);
        });
    }

    public LiveData<List<PublicUserDetailsEntity>> getParticipants() {
        return participants;
    }
}
