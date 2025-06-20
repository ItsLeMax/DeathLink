package de.fpm_studio.deathlink.listener;

import de.fpm_studio.deathlink.DeathLink;
import de.fpm_studio.deathlink.util.WorldGenHandler;
import de.fpm_studio.ilmlib.libraries.ConfigLib;
import de.fpm_studio.ilmlib.util.Template;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Handles the death related processes
 *
 * @author ItsLeMax
 * @since 1.0.0
 */
@RequiredArgsConstructor
public final class OnDeathListener implements Listener {

    private final DeathLink instance;

    private boolean triggered;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        // Trigger plugin only once

        if (triggered)
            return;

        triggered = true;

        final ConfigLib configLib = instance.getConfigLib();
        final WorldGenHandler worldGenHandler = instance.getWorldGenHandler();

        final FileConfiguration config = configLib.getConfig("config");
        final Player deadPlayer = event.getEntity();

        worldGenHandler.setWorld(deadPlayer.getWorld());

        // Different death actions, set inside the config

        for (final Player player : Bukkit.getOnlinePlayers()) {

            final String deathStyle = config.getString("deathStyle");

            switch (deathStyle) {
                case "spectator":
                    player.setHealth(20);
                    player.setGameMode(GameMode.SPECTATOR);
                    break;
                case "killall":
                    player.setHealth(0);
                    break;
                case null, default:
                    throw new IllegalStateException("the death style inside the config does not exist: " + deathStyle + ".");

            }

            // Sound and custom info

            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);

            instance.getMessageLib().sendInfo(player, Template.ERROR, configLib.text("playerDeath")
                    .replace("%p%", deadPlayer.getName())
                    .replace("%r%", "\n§7" + event.getDeathMessage())
            );

        }

        event.setDeathMessage(null);

        if (!config.getBoolean("resetWorldOnDeath"))
            return;

        // Reset world if chosen
        // -1 so the world will generate after a manual stop (see config)

        final int[] timeUntilReset = {worldGenHandler.getTimeUntilReset()};

        if (timeUntilReset[0] == -1)
            return;

        // New gen after time

        Bukkit.getScheduler().scheduleSyncDelayedTask(instance, worldGenHandler::initiate, timeUntilReset[0] * 20L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {

            timeUntilReset[0]--;

            switch (timeUntilReset[0]) {
                case 600, 300, 180, 60, 30, 10, 5, 4, 3, 2, 1 -> Bukkit.broadcastMessage(
                        "§3" + configLib.text("generate").replace("%t%", "§c" + timeUntilReset[0])
                );
            }

        }, 0, 20);

    }

}