package club.partymaker.partymaker.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import club.partymaker.partymaker.party.PartyEntity;
import club.partymaker.partymaker.party.PartyRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PartyListViewModel extends ViewModel {
    private final MutableLiveData<Long> timeInMillis;
    private final LiveData<List<PartyEntity>> partiesBefore;
    private final LiveData<List<PartyEntity>> partiesAfter;

    @Inject
    public PartyListViewModel(PartyRepository partyRepository) {
        timeInMillis = new MutableLiveData<>(System.currentTimeMillis());
        partiesBefore = Transformations.switchMap(timeInMillis, partyRepository::findPartiesBefore);
        partiesAfter = Transformations.switchMap(timeInMillis, partyRepository::findPartiesAfter);
    }

    public void updateCurrentTime() {
        timeInMillis.setValue(System.currentTimeMillis());
    }

    public LiveData<List<PartyEntity>> getPastParties() {
        return partiesBefore;
    }

    public LiveData<List<PartyEntity>> getUpcomingParties() {
        return partiesAfter;
    }
}
