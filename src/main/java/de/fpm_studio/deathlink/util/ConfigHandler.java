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

        ERROR = configLib.text("commands.error");

        STATE_TOGGLED = configLib.text("commands.state-toggled");
        RESET = configLib.text("commands.reset");
        NO_RESET = configLib.text("commands.no-reset");

        NO_PERMISSIONS = configLib.text("commands.no-permissions");
        PLAYER_DEATH = configLib.text("events.player-death");
        GENERATE = configLib.text("events.generate");

        DEATH_STYLE = config.getString("deathStyle");
        RESET_WORLD_ON_DEATH = config.getBoolean("resetWorldOnDeath");

    }

    public static String ERROR;

    private static String STATE_TOGGLED, RESET, NO_RESET;

    public static String NO_PERMISSIONS, PLAYER_DEATH, GENERATE;

    public static String DEATH_STYLE;
    public static boolean RESET_WORLD_ON_DEATH;

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

        final String listenerEnabled = (IS_LISTENER_ENABLED ? "§c" + RESET : "§a" + NO_RESET) + "§7";

        return "§3" + STATE_TOGGLED
                .replaceFirst("%c% ", "§7")
                .replaceFirst("%s%", listenerEnabled)
                .replaceFirst("%c% ", "§e")
                .replaceFirst(" %c%", "§7");

    }

}