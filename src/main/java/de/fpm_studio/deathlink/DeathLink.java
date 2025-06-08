package de.fpm_studio.deathlink;

import de.fpm_studio.deathlink.events.OnDeathListener;
import de.fpm_studio.deathlink.util.WorldGenHandler;
import de.fpm_studio.ilmlib.libraries.ConfigLib;
import de.fpm_studio.ilmlib.libraries.MessageLib;
import de.fpm_studio.ilmlib.util.Template;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Contains the entry point of the plugin
 *
 * @author ItsLeMax
 * @since 1.0.0
 */
@Getter
public final class DeathLink extends JavaPlugin {

    private ConfigLib configLib;
    private MessageLib messageLib;

    private WorldGenHandler worldGenHandler;

    private int timeUntilReset;

    @Override
    public void onEnable() {

        // Initializing the own lib for config and message creation

        configLib = new ConfigLib(this)
                .createDefaultConfigs("config")
                .createConfigsInsideDirectory("languages", "de_DE", "en_US", "custom_lang");

        messageLib = new MessageLib()
                .addSpacing()
                .setPrefix("§3DeathLink §7»")
                .setFormattingCode(Template.ERROR, 'c');

        // World gen class initialization

        intializeWorldGen();

        // Event registration and plugin message

        registerEvents();

        final String initialMessage = "§3" + getConfigLib().text("init")
                .replace("%p%", "[DeathLink]");

        Bukkit.getConsoleSender().sendMessage(initialMessage);

    }

    @Override
    public void onDisable() {

        // Generates a new world on manual server stop if set in the config

        if (getTimeUntilReset() != -1)
            return;

        getWorldGenHandler().initiate();

    }

    /**
     * Initializes the world gen class
     *
     * @author ItsLeMax
     * @since 1.0.0
     */
    private void intializeWorldGen() {

        this.timeUntilReset = getConfigLib().getConfig("config").getInt("timeUntilWorldReset");
        final boolean archiveWorld = getConfigLib().getConfig("config").getBoolean("archiveWorld");

        this.worldGenHandler = new WorldGenHandler();
        this.worldGenHandler.setConfigValues(getTimeUntilReset(), archiveWorld);

    }

    /**
     * Registers the plugins events
     *
     * @author ItsLeMax
     * @since 1.0.0
     */
    private void registerEvents() {

        final OnDeathListener onDeathListener = new OnDeathListener(this);
        getServer().getPluginManager().registerEvents(onDeathListener, this);

    }

}