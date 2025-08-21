package de.fpm_studio.deathlink.commands;

import de.fpm_studio.deathlink.DeathLink;
import de.fpm_studio.deathlink.util.ConfigHandler;
import de.fpm_studio.ilmlib.libraries.MessageLib;
import de.fpm_studio.ilmlib.util.Template;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Holds the {@code toggle death link} command, which allows to toggle the plugins main functionality
 *
 * @author ItsLeMax
 * @since 1.0.0
 */
public final class ToggleDeathLink implements CommandExecutor {

    private final MessageLib messageLib;

    private final ConfigHandler configHandler;

    public ToggleDeathLink(@NotNull final DeathLink instance) {

        messageLib = instance.getMessageLib();

        configHandler = instance.getConfigHandler();

    }

    public static final String PERMISSION_TOGGLE = "deathlink.toggle";

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {

        if (!sender.hasPermission(PERMISSION_TOGGLE)) {
            messageLib.sendInfo(sender, Template.ERROR, ConfigHandler.NO_PERMISSIONS);
            return true;
        }

        ConfigHandler.IS_LISTENER_ENABLED = !ConfigHandler.IS_LISTENER_ENABLED;

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(configHandler.info());
        Bukkit.broadcastMessage("");

        return true;

    }

}