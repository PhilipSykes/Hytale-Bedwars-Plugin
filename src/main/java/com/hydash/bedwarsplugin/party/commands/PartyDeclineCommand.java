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

import java.awt.*;
import java.util.UUID;

public class PartyDeclineCommand extends AbstractPlayerCommand {

    public PartyDeclineCommand() {
        super("decline", "Decline the most recent party invite.");
    }

    @Override
    protected void execute(
            @NotNull CommandContext commandContext,
            @NotNull Store<EntityStore> store,
            @NotNull Ref<EntityStore> ref,
            @NotNull PlayerRef playerRef,
            @NotNull World world
    ) {
        UUID playerUuid = playerRef.getUuid();

        try {
            PartyManager partyManager = ExamplePlugin.PARTY_MANAGER;
            Party party = partyManager.getInvitedParty(playerUuid);

            partyManager.declineInvite(playerUuid);

            playerRef.sendMessage(
                    Message.raw("Party invite declined.").color(Color.RED)
            );
            party.getLeader().sendMessage(Message.raw(String.format("%s has declined your party invite!", playerRef.getUsername())).color(Color.RED));

        } catch (IllegalArgumentException e) {
            playerRef.sendMessage(Message.raw(e.getMessage()).color(Color.RED));
        }
    }
}
