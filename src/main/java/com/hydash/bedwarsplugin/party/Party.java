package com.hydash.bedwarsplugin.party;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Party {

    private UUID leader;
    private final Set<UUID> members = new HashSet<>();

    public Party(UUID leader) {
        this.leader = leader;
    }

    public void addPlayer(UUID player) {
        if (player.equals(leader)) {
            throw new IllegalArgumentException("Player is already the party leader!");
        }
        if (members.contains(player)) {
            throw new IllegalArgumentException("Player is already in the party!");
        }
        members.add(player);
    }

    public void removePlayer(UUID player) {
        if (player.equals(leader)) {
            throw new IllegalArgumentException("Party leader cannot be removed!");
        }
        if (!members.contains(player)) {
            throw new IllegalArgumentException("Player is not in this party!");
        }
        members.remove(player);
    }

    public void promotePlayer(UUID leader, UUID player) {
        if (!leader.equals(this.leader)) {
            throw new IllegalArgumentException("You must be party leader to use this command!");
        }
        if (!members.contains(player)) {
            throw new IllegalArgumentException("Player is not a member of this party!");
        }
        if (leader.equals(player)) {
            throw new IllegalArgumentException("You can't promote yourself!");
        }

        members.remove(player);
        members.add(this.leader);
        this.leader = player;
    }

    public boolean isNotLeader(UUID player) {
        return !player.equals(leader);
    }

    public int getPartySize() {
        return members.size() + 1;
    }

    public PlayerRef getLeader() {
        Universe universe = Universe.get();
        return universe.getPlayer(leader);
    }

    public Set<UUID> getPlayers() {
        Set<UUID> players = new HashSet<>(members);
        players.add(leader);
        return players;
    }

    public void broadcast(String message) {
        Universe universe = Universe.get();
        for (UUID uuid : getPlayers()) {
            PlayerRef player = universe.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(Message.raw(message).color(Color.ORANGE));
            }
        }
    }
}

//TODO: party needs to listen for disconnecting players and then remove them after 5 mins. Also needs to listen for players connecting