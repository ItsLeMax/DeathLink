package de.fpm_studio.deathlink.util;

import de.fpm_studio.deathlink.DeathLink;
import de.fpm_studio.ilmlib.libraries.ConfigLib;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * Handles everything config related
 *
 * @author ItsLeMax
 * @since 1.0.0
 */
public final class ConfigHandler {

    public ConfigHandler(@NotNull final DeathLink instance) {

        final ConfigLib configLib = instance.getConfigLib();
        final FileConfiguration config = instance.getConfig();

        COMMANDS_ERROR = configLib.text("commands.error");

        COMMANDS_STATE_TOGGLED = configLib.text("commands.state-toggled");
        COMMANDS_RESET = configLib.text("commands.reset");
        COMMANDS_NO_RESET = configLib.text("commands.no-reset");

        COMMANDS_NO_PERMISSIONS = configLib.text("commands.no-permissions");
        EVENTS_PLAYER_DEATH = configLib.text("events.player-death");
        EVENTS_GENERATE = configLib.text("events.generate");

        DEATH_STYLE = config.getString("deathStyle");
        IS_WORLD_RESET_ENABLED = config.getBoolean("resetWorldOnDeath");

    }

    public static String COMMANDS_ERROR;

    private static String COMMANDS_STATE_TOGGLED, COMMANDS_RESET, COMMANDS_NO_RESET;

    public static String COMMANDS_NO_PERMISSIONS, EVENTS_PLAYER_DEATH, EVENTS_GENERATE;

    public static String DEATH_STYLE;
    public static boolean IS_WORLD_RESET_ENABLED;

    public static boolean INITIALIZE_WORLD_GENERATION, IS_LISTENER_ENABLED;

    /**
     * Creates an information about the plugins state
     *
     * @return {@link String} with information about the current state.
     *
     * @author ItsLeMax
     * @since 1.0.0
     */
    public String info() {

        final String listenerEnabled = (IS_LISTENER_ENABLED ? "§c" + COMMANDS_RESET : "§a" + COMMANDS_NO_RESET) + "§7";

        return "§3" + COMMANDS_STATE_TOGGLED
                .replaceFirst("%c% ", "§7")
                .replaceFirst("%s%", listenerEnabled)
                .replaceFirst("%c% ", "§e")
                .replaceFirst(" %c%", "§7");

    }

}