package com.hydash.bedwarsplugin;

import com.hydash.bedwarsplugin.party.PartyManager;
import com.hydash.bedwarsplugin.party.commands.PartyCommand;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

public class ExamplePlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    public static PartyManager PARTY_MANAGER;

    public ExamplePlugin(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
        PARTY_MANAGER = new PartyManager();
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new PartyCommand());
    }
}
