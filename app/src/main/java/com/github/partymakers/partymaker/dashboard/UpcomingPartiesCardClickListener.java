package com.github.partymakers.partymaker.dashboard;

import com.github.partymakers.partymaker.party.PartyEntity;

@FunctionalInterface
public interface UpcomingPartiesCardClickListener {
    void onCardClicked(PartyEntity partyEntity);
}
