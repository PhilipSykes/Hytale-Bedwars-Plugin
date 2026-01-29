package com.hydash.bedwarsplugin.party;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

public class PartyManager {

    private final Map<UUID, Party> partyByPlayer = new HashMap<>();
    private final Map<UUID, Stack<Party>> outgoingInvites = new HashMap<>();

    public Party createParty(UUID leader) {
        ensureNotInParty(leader);

        Party party = new Party(leader);
        partyByPlayer.put(leader, party);

        return party;
    }

    public Party getParty(UUID player) {
            return partyByPlayer.get(player);
    }

    public Party getInvitedParty(UUID player) {
        Stack<Party> stack = outgoingInvites.get(player);
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }

    public void invitePlayer(Party party, UUID target) {
        ensureNotInParty(target);
        outgoingInvites
                .computeIfAbsent(target, k -> new Stack<>())
                .push(party);
    }

    public Party acceptInvite(UUID player) {
        Stack<Party> stack = outgoingInvites.get(player);
        if (stack == null || stack.isEmpty()) {
            throw new IllegalStateException("No pending invites");
        }

        Party party = stack.pop();
        if (stack.isEmpty()) {
            outgoingInvites.remove(player);
        }

        party.addPlayer(player);
        partyByPlayer.put(player, party);

        return party;
    }

    public void declineInvite(UUID player) {
        Stack<Party> stack = outgoingInvites.get(player);
        if (stack == null || stack.isEmpty()) {
            throw new IllegalStateException("No pending invites");
        }

        stack.pop();
        if (stack.isEmpty()) {
            outgoingInvites.remove(player);
        }
    }

//    public void addPlayer(Party party, UUID player) {
//        ensureNotInParty(player);
//        party.addPlayer(player);
//        partyByPlayer.put(player, party);
//    }

    public void removePlayer(Party party, UUID player) {
        party.removePlayer(player);
        partyByPlayer.remove(player);
    }

    public void disbandParty(Party party) {
        partyByPlayer.entrySet().removeIf(e -> e.getValue() == party);
        outgoingInvites.values().forEach(stack -> stack.removeIf(p -> p == party));
        outgoingInvites.entrySet().removeIf(e -> e.getValue().isEmpty());
    }

    public void promotePlayer(Party party, UUID leader, UUID player) {
        party.promotePlayer(leader, player);
    }

    private void ensureNotInParty(UUID player) {
        if (partyByPlayer.containsKey(player)) {
            throw new IllegalArgumentException("Player is already in a party!");
        }
    }
}
