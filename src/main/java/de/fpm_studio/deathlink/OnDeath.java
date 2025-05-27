package de.fpm_studio.deathlink;

import de.max.ilmlib.libraries.MessageLib;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static de.fpm_studio.deathlink.DeathLink.configLib;
import static de.fpm_studio.deathlink.DeathLink.messageLib;

public class OnDeath implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) throws Exception {
        FileConfiguration config = configLib.getConfig("config");

        Player deadPlayer = event.getEntity();
        deadPlayer.setHealth(20);

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
                    throw new IllegalStateException("the death style inside the config does not exist: " + deathStyle + ".");
            }

            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);

            messageLib.sendInfo(player, MessageLib.Template.ERROR, configLib.lang("playerDeath")
                    .replace("%p%", deadPlayer.getName())
                    .replace("%r%", "\n§7" + event.getDeathMessage())
            );

            event.setDeathMessage(null);

            if (config.getBoolean("resetWorldOnDeath")) {
                // String worldName = Bukkit.getWorlds().getFirst().getName();
                String worldType = config.getString("worldType");

                if (worldType == null) {
                    throw new Exception("the worlds type inside the config cannot be null.");
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(DeathLink.plugin, () -> {
                    // WorldCreator worldCreator = new WorldCreator(worldName);

                    // worldCreator.environment(World.Environment.valueOf(worldType));
                    // worldCreator.type(WorldType.valueOf(worldType));

                    // worldCreator.createWorld();

                    // Server-Stop, wenn die Welt fertig generiert wurde
                }, config.getInt("timeUntilWorldReset"));
            }
        }
    }
}