package de.fpm_studio.deathlink.listener;

import de.fpm_studio.deathlink.DeathLink;
import de.fpm_studio.deathlink.util.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Handles players joining the server
 *
 * @author ItsLeMax
 * @since 1.0.0
 */
public final class PlayerJoinListener implements Listener {

    private final ConfigHandler configHandler;

    public PlayerJoinListener(@NotNull final DeathLink instance) {
        configHandler = instance.getConfigHandler();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        event.getPlayer().sendMessage("");
        event.getPlayer().sendMessage(configHandler.info());
        event.getPlayer().sendMessage("");

    }

}