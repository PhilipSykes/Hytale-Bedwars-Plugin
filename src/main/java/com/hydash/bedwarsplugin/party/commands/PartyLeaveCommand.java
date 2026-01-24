package com.hydash.bedwarsplugin.party.commands;

import com.hydash.bedwarsplugin.ExamplePlugin;
import com.hydash.bedwarsplugin.party.Party;
import com.hydash.bedwarsplugin.party.PartyManager;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyLeaveCommand extends AbstractPlayerCommand {

    public PartyLeaveCommand() {
        super("leave", "Leave current party.");
    }

    @Override
    protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
        UUID senderUuid = playerRef.getUuid();

        try {
            PartyManager partyManager = ExamplePlugin.PARTY_MANAGER;

            Party party = partyManager.getParty(senderUuid);

            if (party == null) {
                throw new IllegalArgumentException(
                        "You are not in a Party!"
                );
            } else {
                partyManager.removePlayer(party, senderUuid);
                playerRef.sendMessage(Message.raw("You have left the party."));

                //TODO: Broadcast to party members that player has left the party
            }
        } catch (IllegalArgumentException e) {
            playerRef.sendMessage(
                    Message.raw(e.getMessage())
            );
        }
    }
}