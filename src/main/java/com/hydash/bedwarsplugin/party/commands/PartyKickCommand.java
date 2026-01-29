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

import java.awt.*;
import java.util.UUID;

public class PartyKickCommand extends AbstractPlayerCommand {
    private final RequiredArg<PlayerRef> targetPlayerRef;

    public PartyKickCommand() {
        super("kick", "Remove a player from your party.");
        this.targetPlayerRef = this.withRequiredArg(
                "Player",
                "Player to remove",
                ArgTypes.PLAYER_REF
        );
    }

    @Override
    protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
        try {
            PlayerRef targetPlayerRef = commandContext.get(this.targetPlayerRef);
            PartyManager partyManager = ExamplePlugin.PARTY_MANAGER;
            Party party = partyManager.getParty(playerRef.getUuid());


            if (targetPlayerRef.equals(playerRef)) {
                throw new IllegalArgumentException(
                        "You cannot kick yourself from the party."
                );
            } else {
                targetPlayerRef.sendMessage(Message.raw(String.format("You have been kicked from %s's party!", playerRef.getUsername())).color(Color.RED));
                playerRef.sendMessage(Message.raw(String.format("You have kicked %s from the party!", targetPlayerRef.getUsername())).color(Color.ORANGE));
                partyManager.removePlayer(party, targetPlayerRef.getUuid());
            }

        } catch (IllegalArgumentException e) {
            playerRef.sendMessage(
                    Message.raw(e.getMessage()).color(Color.RED)
            );
        }
    }
}
