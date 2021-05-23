package club.partymaker.partymaker.dashboard;

import club.partymaker.partymaker.party.PartyEntity;

@FunctionalInterface
public interface PartyListCardClickListener {
    void onCardClicked(PartyEntity partyEntity);
}
