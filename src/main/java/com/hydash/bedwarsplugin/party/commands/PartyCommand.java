package com.hydash.bedwarsplugin.party.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;


public class PartyCommand extends AbstractCommandCollection {
    public PartyCommand() {
        super("party", "Commands for creating and managing a party.");
        this.addSubCommand(new PartyAddCommand());
        this.addSubCommand(new PartyDisbandCommand());
        this.addSubCommand(new PartyLeaveCommand());
        this.addSubCommand(new PartyPromoteCommand());
    }
}
