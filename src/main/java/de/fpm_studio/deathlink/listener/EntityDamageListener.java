package de.fpm_studio.deathlink.listener;

import de.fpm_studio.deathlink.DeathLink;
import de.fpm_studio.deathlink.util.ConfigHandler;
import de.fpm_studio.ilmlib.libraries.MessageLib;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles the death related processes
 *
 * @author ItsLeMax
 * @since 1.0.0
 */
public final class EntityDamageListener implements Listener {

    private final DeathLink instance;

    private final MessageLib messageLib;

    public EntityDamageListener(@NotNull final DeathLink instance) {

        this.instance = instance;

        messageLib = instance.getMessageLib();

    }

    @EventHandler
    public void onDeath(EntityDamageEvent event) {

        // Trigger world generation process on a players death once if enabled

        if (!ConfigHandler.IS_LISTENER_ENABLED || ConfigHandler.INITIALIZE_WORLD_GENERATION)
            return;

        if (!(event.getEntity() instanceof Player targetPlayer))
            return;

        if (targetPlayer.getHealth() - event.getDamage() > 0)
            return;

        ConfigHandler.INITIALIZE_WORLD_GENERATION = true;

        // Different death actions, set inside the config

        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {

            switch (ConfigHandler.DEATH_STYLE) {

                case "spectator":
                    event.setCancelled(true);

                    onlinePlayer.setHealth(20);
                    onlinePlayer.setGameMode(GameMode.SPECTATOR);
                    break;

                case "killall":
                    onlinePlayer.setHealth(0);
                    break;

            }

            // Sound and custom death message

            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);

            messageLib.sendInfo(onlinePlayer, 'c', ConfigHandler.EVENTS_PLAYER_DEATH
                    .replace("%p%", targetPlayer.getName())
                    .replace("%r%", event.getCause().name().toLowerCase().replaceAll("_", " "))
            );

        }

        // Reset world if chosen

        if (!ConfigHandler.IS_WORLD_RESET_ENABLED)
            return;

        final AtomicInteger timeUntilReset = new AtomicInteger(instance.getTimeUntilReset());

        if (timeUntilReset.get() < 0)
            return;

        // New gen after time

        Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {

            switch (timeUntilReset.get()) {

                case 600, 300, 180, 60, 30, 10, 5, 4, 3, 2, 1 -> {

                    Bukkit.broadcastMessage("§3" + ConfigHandler.EVENTS_GENERATE
                            .replace("%t%", "§c" + timeUntilReset)
                    );

                    for (final Player onlinePlayer : Bukkit.getOnlinePlayers())
                        onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);

                }

                case 0 -> generateNewWorld();

            }

            timeUntilReset.set(timeUntilReset.get() - 1);

        }, 0, 20);

    }

    /**
     * Initiates the world generation process by deleting or renaming the target world, as chosen inside the config, and
     * stopping the server
     *
     * @author ItsLeMax
     * @since 1.0.0
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void generateNewWorld() {

        final UUID randomUUID = UUID.randomUUID();

        // Target every world that is registered (by default overworld, nether and end)

        for (final World world : Bukkit.getWorlds()) {

            final String worldPath = Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/" + world.getName();
            final File worldFolder = new File(worldPath);

            // Rename worlds folder or delete, depending on config setting

            final String newWorldName = world.getName() + "_" + randomUUID;
            final File futureDirectory = new File(worldFolder.getParent() + "\\" + newWorldName);

            worldFolder.renameTo(futureDirectory);

        }

        // Stop the server right after to generate and load a new world

        Bukkit.getServer().shutdown();

    }

}