package de.max.deathlink.init;

import de.max.ilmlib.init.ILMLib;
import de.max.ilmlib.libraries.ConfigLib;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeathLink extends JavaPlugin {
    public static ConfigLib configLib;
    public static DeathLink plugin;

    @Override
    public void onEnable() {
        plugin = this;

        configLib = new ILMLib(this).getConfigLib();
        configLib
                .createDefaults("config")
                .createInsideDirectory("languages", "de_DE", "en_US", "custom_lang");

        getServer().getPluginManager().registerEvents(new OnDeath(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
