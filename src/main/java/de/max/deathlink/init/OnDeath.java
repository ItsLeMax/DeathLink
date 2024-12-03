package de.max.deathlink.init;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static de.max.deathlink.init.DeathLink.configLib;

public class OnDeath implements Listener {
    FileConfiguration config = configLib.getConfig("config");

    @EventHandler
    public void onDeath(PlayerDeathEvent event) throws Exception {
        Player diedPlayer = event.getEntity();
        diedPlayer.setHealth(20);

        for (Player player : Bukkit.getOnlinePlayers()) {
            String deathStyle = config.getString("deathStyle");

            switch (deathStyle) {
                case "spectator":
                    player.setGameMode(GameMode.SPECTATOR);
                    break;
                case "killall":
                    player.setHealth(0);
                    break;
                case null, default:
                    throw new IllegalStateException("written deathStyle does not exist: " + deathStyle);
            }

            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);

            player.sendMessage("");
            player.sendMessage("§8> §3DeathLink");
            player.sendMessage("§c" + configLib.lang("playerDeath")
                    .replace("%p%", diedPlayer.getName())
                    .replace("%r%", "\n§7" + event.getDeathMessage())
            );
            player.sendMessage("");

            event.setDeathMessage(null);

            if (config.getBoolean("resetWorldOnDeath")) {
                String worldName = config.getString("worldName");
                if (worldName == null) {
                    throw new Exception("the worlds name inside the config cannot be null");
                }

                String worldType = config.getString("worldType");
                if (worldType == null) {
                    throw new Exception("the worlds type inside the config cannot be null");
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(DeathLink.plugin, () -> {
                    // WorldCreator worldCreator = new WorldCreator(worldName);

                    // worldCreator.environment(World.Environment.valueOf(worldType));
                    // worldCreator.type(WorldType.valueOf(worldType));

                    // worldCreator.createWorld();

                    // Server-Stop, wenn die Welt fertig generiert wurde
                    // GitHub Problem durch "fremden Owner" (siehe "Commit" Tab - ALT+0 - unten links)
                    // ChatGPT nach universeller Lösung fragen statt 1:1 den vorgeschlagenen Befehl
                }, config.getInt("timeUntilWorldReset"));
            }
        }
    }
}