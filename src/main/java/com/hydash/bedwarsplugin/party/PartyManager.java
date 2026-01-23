package com.hydash.bedwarsplugin.party;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PartyManager {

    private final Map<UUID, Party> partyByPlayer = new HashMap<>();

    public Party createParty(UUID leader, UUID member) {
        if (leader.equals(member)) {
            throw new IllegalArgumentException("You can't create a party with yourself!");
        }

        ensureNotInParty(leader);
        ensureNotInParty(member);

        Party party = new Party(leader, member);
        partyByPlayer.put(leader, party);
        partyByPlayer.put(member, party);

        return party;
    }


    public Party getParty(UUID player) {
        return partyByPlayer.get(player);
    }

    public void addPlayer(Party party, UUID player) {
        ensureNotInParty(player);
        party.addPlayer(player);
        partyByPlayer.put(player, party);
    }

    public void removePlayer(Party party, UUID player) {
        party.removePlayer(player);
        partyByPlayer.remove(player);
    }

    public void disbandParty(Party party) {
        partyByPlayer.entrySet()
                .removeIf(e -> e.getValue() == party);
    }

    private void ensureNotInParty(UUID player) {
        if (partyByPlayer.containsKey(player)) {
            throw new IllegalArgumentException("Player is already in a party!");
        }
    }
}
