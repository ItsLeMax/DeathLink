package de.fpm_studio.deathlink;

import de.fpm_studio.deathlink.commands.ToggleDeathLink;
import de.fpm_studio.deathlink.listener.EntityDamageListener;
import de.fpm_studio.deathlink.listener.PlayerJoinListener;
import de.fpm_studio.deathlink.util.ConfigHandler;
import de.fpm_studio.ilmlib.libraries.ConfigLib;
import de.fpm_studio.ilmlib.libraries.MessageLib;
import de.fpm_studio.ilmlib.util.Template;
import lombok.Getter;
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

    private ConfigHandler configHandler;

    private int timeUntilReset;

    @Override
    public void onEnable() {

        // Initializing the own lib for config and message creation

        configLib = new ConfigLib(this)
                .createDefaultConfigs("config")
                .createConfigsInsideDirectory("localization", "de_DE", "en_US", "custom");

        configHandler = new ConfigHandler(this);

        messageLib = new MessageLib()
                .addSpacing()
                .setPrefix("§3DeathLink §7»")
                .createTemplateDefaults()
                .setSuffix(Template.ERROR, ConfigHandler.COMMANDS_ERROR);

        // World generator logic initialization

        this.timeUntilReset = getConfigLib().getConfig("config").getInt("timeUntilWorldReset");

        register();

    }

    /**
     * Registers commands and events
     *
     * @author ItsLeMax
     * @since 1.0.0
     */
    @SuppressWarnings("ConstantConditions")
    private void register() {

        getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        getCommand("toggledeathlink").setExecutor(new ToggleDeathLink(this));

    }

}