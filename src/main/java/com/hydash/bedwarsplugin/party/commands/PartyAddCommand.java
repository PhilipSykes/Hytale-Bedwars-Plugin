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

public class PartyAddCommand extends AbstractPlayerCommand {

    private final RequiredArg<PlayerRef> targetPlayerRef;

    public PartyAddCommand() {
        super("add", "Invite a player to your party.");
        this.targetPlayerRef = this.withRequiredArg(
                "Player",
                "Player to invite",
                ArgTypes.PLAYER_REF
        );
    }

    @Override
    protected void execute(
            @NotNull CommandContext commandContext,
            @NotNull Store<EntityStore> store,
            @NotNull Ref<EntityStore> ref,
            @NotNull PlayerRef playerRef,
            @NotNull World world
    ) {
        PlayerRef targetPlayerRef = commandContext.get(this.targetPlayerRef);
        UUID senderUuid = playerRef.getUuid();
        UUID targetUuid = targetPlayerRef.getUuid();

        try {
            PartyManager partyManager = ExamplePlugin.PARTY_MANAGER;

            Party party = partyManager.getParty(senderUuid);

            if (party == null) {
                party = partyManager.createParty(senderUuid);
            }

            if (party.isNotLeader(senderUuid)) {
                throw new IllegalArgumentException(
                        "You must be the party leader to invite players!"
                );
            }

            partyManager.invitePlayer(party, targetUuid);

            playerRef.sendMessage(
                    Message.raw(String.format(
                            "You invited %s to the party.",
                            targetPlayerRef.getUsername()
                    )).color(Color.ORANGE)
            );

            targetPlayerRef.sendMessage(
                    Message.raw(String.format(
                            "%s has invited you to a party. Use /party accept or /party decline.",
                            playerRef.getUsername()
                    )).color(Color.ORANGE)
            );

        } catch (IllegalArgumentException e) {
            playerRef.sendMessage(Message.raw(e.getMessage()).color(Color.RED));
        }
    }
}
