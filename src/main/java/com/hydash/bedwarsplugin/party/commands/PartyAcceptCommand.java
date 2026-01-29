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

public class PartyAcceptCommand extends AbstractPlayerCommand {

    public PartyAcceptCommand() {
        super("accept", "Accept the most recent party invite.");
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

            Party party = partyManager.acceptInvite(playerUuid);

            party.broadcast(String.format(
                    "%s has joined the party!",
                    playerRef.getUsername()
            ));

            playerRef.sendMessage(
                    Message.raw("You joined the party.").color(Color.ORANGE)
            );

        } catch (IllegalArgumentException e) {
            playerRef.sendMessage(Message.raw(e.getMessage()).color(Color.RED));
        }
    }
}
