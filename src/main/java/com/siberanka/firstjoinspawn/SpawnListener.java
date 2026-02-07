package com.siberanka.firstjoinspawn;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class SpawnListener implements Listener {

    private final FirstJoinSpawnPlugin plugin;

    public SpawnListener(FirstJoinSpawnPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();

        if (!config.getBoolean("first-join.enabled", true)) {
            return;
        }

        if (player.hasPlayedBefore()) {
            return;
        }

        Location spawnLocation = getConfiguredSpawn();
        if (spawnLocation == null) {
            send(player, "messages.spawn-not-set");
            return;
        }

        long delaySeconds = Math.max(0L, config.getLong("first-join.teleport-delay-seconds", 3L));
        long delayTicks = delaySeconds * 20L;
        String waitingMessage = config.getString("messages.first-join-waiting", "");
        if (!waitingMessage.isBlank()) {
            send(player, "messages.first-join-waiting", "{seconds}", String.valueOf(delaySeconds));
        }

        player.getScheduler().runDelayed(plugin, (ScheduledTask task) ->
                        player.teleportAsync(spawnLocation).thenAccept(success -> {
                            if (success) {
                                send(player, "messages.first-join-teleported");
                            } else {
                                send(player, "messages.teleport-failed");
                            }
                        }),
                null,
                delayTicks
        );
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        FileConfiguration config = plugin.getConfig();
        if (!config.getBoolean("respawn.redirect-no-bed-to-spawn", true)) {
            return;
        }

        if (event.isBedSpawn() || event.isAnchorSpawn()) {
            return;
        }

        Location spawnLocation = getConfiguredSpawn();
        if (spawnLocation == null) {
            return;
        }

        event.setRespawnLocation(spawnLocation);
    }

    private Location getConfiguredSpawn() {
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

    private void send(Player player, String path) {
        send(player, path, null, null);
    }

    private void send(Player player, String path, String placeholder, String value) {
        String prefix = plugin.getConfig().getString("messages.prefix", "&7[&bFJS&7] ");
        String message = plugin.getConfig().getString(path, "");
        if (message.isBlank()) {
            return;
        }
        if (placeholder != null && value != null) {
            message = message.replace(placeholder, value);
        }
        player.sendMessage(colorize(prefix + message));
    }

    private String colorize(String input) {
        return input.replace('&', '§');
    }
}
