package de.fpm_studio.deathlink;

import de.fpm_studio.deathlink.events.OnDeathListener;
import de.fpm_studio.deathlink.util.WorldGenHandler;
import de.fpm_studio.ilmlib.libraries.ConfigLib;
import de.fpm_studio.ilmlib.libraries.MessageLib;
import de.fpm_studio.ilmlib.util.Template;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Contains the entry point of the plugin
 *
 * @author ItsLeMax
 * @since 1.0.0
 */
public final class DeathLink extends JavaPlugin {

    private WorldGenHandler worldGenHandler;

    private int timeUntilReset;

    @Override
    public void onEnable() {

        // Initializing the own lib for config and message creation

        final ConfigLib configLib = new ConfigLib(this)
                .createDefaultConfigs("config")
                .createConfigsInsideDirectory("languages", "de_DE", "en_US", "custom_lang");

        final MessageLib messageLib = new MessageLib()
                .addSpacing()
                .setPrefix("§3DeathLink §7»")
                .setFormattingCode(Template.ERROR, 'c');

        // World gen class initialization

        this.timeUntilReset = configLib.getConfig("config").getInt("timeUntilWorldReset");
        final boolean archiveWorld = configLib.getConfig("config").getBoolean("archiveWorld");

        this.worldGenHandler = new WorldGenHandler();
        this.worldGenHandler.setConfigValues(timeUntilReset, archiveWorld);

        // Event registration and plugin message

        getServer().getPluginManager().registerEvents(
                new OnDeathListener(this, configLib, messageLib, worldGenHandler), this
        );

        Bukkit.getConsoleSender().sendMessage(
                "§3" + configLib.text("init").replace("%p%", "[DeathLink]")
        );

    }

    @Override
    public void onDisable() {

        // Generates a new world on manual server stop if set in the config

        if (timeUntilReset != -1)
            return;

        worldGenHandler.initiate();

    }

}