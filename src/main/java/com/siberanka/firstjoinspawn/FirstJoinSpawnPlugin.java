package com.siberanka.firstjoinspawn;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class FirstJoinSpawnPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        FjsCommand commandExecutor = new FjsCommand(this);
        PluginCommand command = getCommand("fjs");
        if (command == null) {
            getLogger().severe("fjs command not found in plugin.yml. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        command.setExecutor(commandExecutor);
        command.setTabCompleter(commandExecutor);
        getServer().getPluginManager().registerEvents(new SpawnListener(this), this);
    }
}
