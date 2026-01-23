package com.hydash.bedwarsplugin.party.commands;

import com.hydash.bedwarsplugin.ExamplePlugin;
import com.hydash.bedwarsplugin.party.Party;
import com.hydash.bedwarsplugin.party.PartyManager;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyAddCommand extends CommandBase {
    private final RequiredArg<PlayerRef> playerRef;

    public PartyAddCommand() {
        super("add", "Add a player to your party.");

        this.playerRef = this.withRequiredArg("Player", "Player to add to party", ArgTypes.PLAYER_REF);
    }

    @Override
    protected void executeSync(@NotNull CommandContext commandContext) {
        PlayerRef targetPlayerRef = commandContext.get(playerRef);
        UUID targetUuid = targetPlayerRef.getUuid();
        UUID senderUuid = commandContext.sender().getUuid();

        try {
            PartyManager partyManager = ExamplePlugin.PARTY_MANAGER;

            Party party = partyManager.getParty(senderUuid);

            if (party == null) {
                partyManager.createParty(senderUuid, targetUuid);
            } else {
                if (!party.isLeader(senderUuid)) {
                    throw new IllegalArgumentException(
                            "You must be the party leader to add players!"
                    );
                }
                partyManager.addPlayer(party, targetUuid);
            }

            String message = String.format("Added %s to party successfully.", targetPlayerRef.getUsername());
            commandContext.sendMessage(
                    Message.raw(message)
            );

        } catch (IllegalArgumentException e) {
            commandContext.sendMessage(
                    Message.raw(e.getMessage())
            );
        }
    }

}

// I shouldn't be able to commit this change to main!!