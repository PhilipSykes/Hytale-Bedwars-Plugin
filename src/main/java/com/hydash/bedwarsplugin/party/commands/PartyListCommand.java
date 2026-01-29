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
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;
import java.util.stream.Collectors;

public class PartyListCommand extends AbstractPlayerCommand {

    public PartyListCommand() {
        super("list", "Lists all the party members.");
    }

    @Override
    protected void execute(
            @NotNull CommandContext commandContext,
            @NotNull Store<EntityStore> store,
            @NotNull Ref<EntityStore> ref,
            @NotNull PlayerRef playerRef,
            @NotNull World world
    ) {
        try {
            PartyManager partyManager = ExamplePlugin.PARTY_MANAGER;
            Party party = partyManager.getParty(playerRef.getUuid());
            if (party == null) {
                throw new IllegalArgumentException("You are not in a party.");
            }

            Universe universe = Universe.get();

            String players = party.getPlayers().stream()
                    .map(universe::getPlayer)
                    .filter(Objects::nonNull)
                    .map(PlayerRef::getUsername)
                    .collect(Collectors.joining(", "));

            String message = String.format("You are in a party with: %s.", players);
            playerRef.sendMessage(Message.raw(message).color(Color.ORANGE));

        } catch (IllegalArgumentException e) {
            playerRef.sendMessage(
                    Message.raw(e.getMessage()).color(Color.RED)
            );
        }
    }
}
