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

public class PartyDisbandCommand extends AbstractPlayerCommand {
    public PartyDisbandCommand() {
        super("disband", "Disband your current party");
    }

    @Override
    protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
        UUID senderUuid = playerRef.getUuid();

        try {
            PartyManager partyManager = ExamplePlugin.PARTY_MANAGER;

            Party party = partyManager.getParty(senderUuid);

            if (party == null) {
                throw new IllegalArgumentException(
                        "No party found!"
                );
            } else {
                if (party.isNotLeader(senderUuid)) {
                    throw new IllegalArgumentException(
                            "You are not the leader of this party!"
                    );
                } else {
                    partyManager.disbandParty(party);
                    // TODO: Broadcast Disbanding to all party members
                }
            }
        } catch (IllegalArgumentException e) {
            playerRef.sendMessage(
                    Message.raw(e.getMessage())
            );
        }
    }
}
