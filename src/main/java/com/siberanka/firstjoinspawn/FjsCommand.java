package com.siberanka.firstjoinspawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class FjsCommand implements CommandExecutor, TabCompleter {

    private final FirstJoinSpawnPlugin plugin;

    public FjsCommand(FirstJoinSpawnPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("fjs.admin")) {
            sendMessage(sender, "messages.no-permission");
            return true;
        }

        if (args.length == 0) {
            sendMessage(sender, "messages.usage");
            return true;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "setspawn":
                return handleSetSpawn(sender);
            case "spawn":
                return handleSpawn(sender);
            case "reload":
                plugin.reloadConfig();
                sendMessage(sender, "messages.reload-success");
                return true;
            default:
                sendMessage(sender, "messages.usage");
                return true;
        }
    }

    private boolean handleSetSpawn(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sendMessage(sender, "messages.player-only");
            return true;
        }

        Location loc = player.getLocation();
        FileConfiguration config = plugin.getConfig();
        config.set("spawn.world", loc.getWorld() != null ? loc.getWorld().getName() : null);
        config.set("spawn.x", loc.getX());
        config.set("spawn.y", loc.getY());
        config.set("spawn.z", loc.getZ());
        config.set("spawn.yaw", loc.getYaw());
        config.set("spawn.pitch", loc.getPitch());
        config.set("spawn.set", true);
        plugin.saveConfig();

        sendMessage(sender, "messages.spawn-set");
        return true;
    }

    private boolean handleSpawn(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sendMessage(sender, "messages.player-only");
            return true;
        }

        Location spawnLocation = getConfiguredSpawn();
        if (spawnLocation == null) {
            sendMessage(sender, "messages.spawn-not-set");
            return true;
        }

        player.teleportAsync(spawnLocation).thenAccept(success -> {
            if (success) {
                sendMessage(player, "messages.spawn-teleported");
            } else {
                sendMessage(player, "messages.teleport-failed");
            }
        });
        return true;
    }

    public Location getConfiguredSpawn() {
        FileConfiguration config = plugin.getConfig();
        if (!config.getBoolean("spawn.set", false)) {
            return null;
        }

        String worldName = config.getString("spawn.world");
        if (worldName == null || worldName.isBlank()) {
            return null;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }

        return new Location(
                world,
                config.getDouble("spawn.x"),
                config.getDouble("spawn.y"),
                config.getDouble("spawn.z"),
                (float) config.getDouble("spawn.yaw"),
                (float) config.getDouble("spawn.pitch")
        );
    }

    public void sendMessage(CommandSender sender, String path) {
        String message = plugin.getConfig().getString(path, "&cMissing message: " + path);
        String prefix = plugin.getConfig().getString("messages.prefix", "&7[&bFJS&7] ");
        if (message.equals(prefix)) {
            sender.sendMessage(colorize(message));
            return;
        }
        sender.sendMessage(colorize(prefix + message));
    }

    public String colorize(String input) {
        return input.replace('&', '§');
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("fjs.admin")) {
            return List.of();
        }

        if (args.length == 1) {
            List<String> subs = List.of("setspawn", "spawn", "reload");
            String current = args[0].toLowerCase(Locale.ROOT);
            List<String> result = new ArrayList<>();
            for (String sub : subs) {
                if (sub.startsWith(current)) {
                    result.add(sub);
                }
            }
            return result;
        }
        return List.of();
    }
}
