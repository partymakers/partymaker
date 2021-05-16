package club.partymaker.partymaker.dashboard;

import club.partymaker.partymaker.party.PartyEntity;

@FunctionalInterface
public interface UpcomingPartiesCardClickListener {
    void onCardClicked(PartyEntity partyEntity);
}
