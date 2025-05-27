package de.fpm_studio.deathlink.events;

import de.fpm_studio.deathlink.DeathLink;
import de.fpm_studio.deathlink.util.WorldGeneration;
import de.fpm_studio.ilmlib.libraries.ConfigLib;
import de.fpm_studio.ilmlib.libraries.MessageLib;
import de.fpm_studio.ilmlib.util.Template;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@AllArgsConstructor
public final class OnDeath implements Listener {

    private final DeathLink instance;

    private final ConfigLib configLib;
    private final MessageLib messageLib;

    private final WorldGeneration worldGeneration;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        final FileConfiguration config = configLib.getConfig("config");
        final Player deadPlayer = event.getEntity();

        worldGeneration.setWorld(deadPlayer.getWorld());

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

            messageLib.sendInfo(player, Template.ERROR, configLib.text("playerDeath")
                    .replace("%p%", deadPlayer.getName())
                    .replace("%r%", "\n§7" + event.getDeathMessage())
            );

            event.setDeathMessage(null);

            if (!config.getBoolean("resetWorldOnDeath"))
                return;

            // Reset world if chosen
            // -1 so, as set inside the config, the world will generate after a manual stop

            final int timeUntilReset = worldGeneration.getTimeUntilReset();

            if (timeUntilReset == -1)
                return;

            // New gen after time

            Bukkit.getScheduler().scheduleSyncDelayedTask(instance, worldGeneration::initiate, timeUntilReset);

        }

    }

}