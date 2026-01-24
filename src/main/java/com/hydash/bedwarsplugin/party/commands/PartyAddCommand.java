package com.hydash.bedwarsplugin.party.commands;

import com.hydash.bedwarsplugin.ExamplePlugin;
import com.hydash.bedwarsplugin.party.Party;
import com.hydash.bedwarsplugin.party.PartyManager;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyAddCommand extends AbstractPlayerCommand {
    private final RequiredArg<PlayerRef> targetPlayerRef;

    public PartyAddCommand() {
        super("add", "Add a player to your party.");

        this.targetPlayerRef = this.withRequiredArg("Player", "Player to add to party", ArgTypes.PLAYER_REF);
    }

    @Override
    protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
        PlayerRef targetPlayerRef = commandContext.get(this.targetPlayerRef);
        UUID targetUuid = targetPlayerRef.getUuid();

        UUID senderUuid = playerRef.getUuid();

        try {
            PartyManager partyManager = ExamplePlugin.PARTY_MANAGER;

            Party party = partyManager.getParty(senderUuid);

            //TODO: add confirmation for invited player
            if (party == null) {
                partyManager.createParty(senderUuid, targetUuid);
            } else {
                if (party.isNotLeader(senderUuid)) {
                    throw new IllegalArgumentException(
                            "You must be the party leader to add players!"
                    );
                }
                partyManager.addPlayer(party, targetUuid);
            }

            // TODO: Change this to broadcast to all party members
            String senderMessage = String.format("Added %s to party successfully.", targetPlayerRef.getUsername());
            playerRef.sendMessage(
                    Message.raw(senderMessage)
            );

            String targetMessage = String.format("You have joined %s's party.", playerRef.getUsername());
            targetPlayerRef.sendMessage(Message.raw(targetMessage));

        } catch (IllegalArgumentException e) {
            playerRef.sendMessage(
                    Message.raw(e.getMessage())
            );
        }
    }
}