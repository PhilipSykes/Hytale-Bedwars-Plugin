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

public class PartyPromoteCommand extends AbstractPlayerCommand {
    private final RequiredArg<PlayerRef> targetPlayerRef;

    public PartyPromoteCommand() {
        super("promote", "Promote a player to party leader!");
        this.targetPlayerRef = this.withRequiredArg("Player", "Player to add to party", ArgTypes.PLAYER_REF);
    }

    @Override
    protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
        UUID senderUuid = playerRef.getUuid();

        PlayerRef targetPlayerRef = commandContext.get(this.targetPlayerRef);
        UUID targetUuid = targetPlayerRef.getUuid();

        try {
            PartyManager partyManager = ExamplePlugin.PARTY_MANAGER;

            Party party = partyManager.getParty(senderUuid);

            if(party == null) {
                throw new IllegalArgumentException(
                        "You are not in a party!"
                );
            } else {
                if (party.isNotLeader(senderUuid)) {
                    throw new IllegalArgumentException(
                            "You must be the party leader to use this command!"
                    );
                } else {
                    partyManager.promotePlayer(party, senderUuid, targetUuid);
                    playerRef.sendMessage(Message.raw(String.format("Successfully promoted %s to party leader!", targetPlayerRef.getUsername())));
                    targetPlayerRef.sendMessage(Message.raw("You are now promoted to party leader!"));
                }
            }
        } catch (IllegalArgumentException e) {
            playerRef.sendMessage(
                    Message.raw(e.getMessage())
            );
        }
    }
}
