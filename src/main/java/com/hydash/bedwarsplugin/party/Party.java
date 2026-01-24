package com.hydash.bedwarsplugin.party;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Party {
    private UUID leader;
    private final Set<UUID> members = new HashSet<>();

    public Party(UUID partyLeader, UUID partyMember ) {
        this.leader = partyLeader;
        this.members.add(partyMember);
    }

    public void addPlayer(UUID player) {
        if (player.equals(leader)) {
            throw new IllegalArgumentException(String.format("%s is already the leader of this party", player));
        }
        if (members.contains(player)) {
            throw new IllegalArgumentException(String.format("%s is already a member of this party!", player));
        }
        members.add(player);
    }

    public void removePlayer(UUID player) {
        if (player.equals(leader)) {
            throw new IllegalArgumentException(String.format("%s is the leader of this party and cannot be removed", player));
        }
        if (!members.contains(player)) {
            throw new IllegalArgumentException(String.format("%s is not a member of this party!", player));
        }
        members.remove(player);
    }

    public void promotePlayer(UUID leader, UUID player) {
        if (!leader.equals(this.leader)) {
            throw new IllegalArgumentException("You must be party leader to use this command!");
        }
        if (!members.contains(player)) {
            throw new IllegalArgumentException(String.format("%s is not a member of this party!", player));
        }
        if (leader.equals(player)) {
            throw new IllegalArgumentException("You can't promote self!");
        }
        members.remove(player);
        members.add(leader);
        this.leader = player;
    }

    public boolean isNotLeader(UUID player) {
        return !player.equals(leader);
    }

    public int getPartySize() {
        return members.size()+1;
    }

    public Set<UUID> getPlayers() {
        Set<UUID> players = new HashSet<>(members);
        players.add(leader);
        return players;
    }
}
