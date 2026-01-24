package com.hydash.bedwarsplugin.party;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Party {
    private UUID leader;
    private final Set<UUID> members = new HashSet<>();

    public Party(UUID partyLeader, UUID partyMember ) {
        this.leader = partyLeader;
        this.members.add(partyMember);

        // This also sucks but oh well
        Universe universe = Universe.get();
        PlayerRef leaderRef = universe.getPlayer(leader);
        PlayerRef memberRef = universe.getPlayer(members.iterator().next());

        assert leaderRef != null;
        assert memberRef != null;
        leaderRef.sendMessage(Message.raw(String.format("You have created a party with %s!", memberRef.getUsername())));
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

    public void BroadcastToParty (String message) {
        Universe universe = Universe.get();
        Set<UUID> memberUuids = new HashSet<>(getPlayers());

        //This kind of sucks but oh well
        for (UUID uuid : memberUuids) {
            PlayerRef player = universe.getPlayer(uuid);
            assert player != null;
            player.sendMessage(Message.raw(message));
        }
    }
}

//TODO: party needs to listen for disconnecting players and then remove them after 5 mins. Also needs to listen for players connecting