package de.max.deathlink.init;

import de.max.ilmlib.libraries.ConfigLib;
import de.max.ilmlib.libraries.MessageLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeathLink extends JavaPlugin {
    public static DeathLink plugin;

    public static ConfigLib configLib;
    public static MessageLib messageLib;

    @Override
    public void onEnable() {
        plugin = this;

        configLib = new ConfigLib(this)
                .createDefaults("config")
                .createInsideDirectory("languages", "de_DE", "en_US", "custom_lang");

        messageLib = new MessageLib()
                .addSpacing()
                .setPrefix("§3DeathLink §7»", true)
                .setFormattingCode(MessageLib.Template.ERROR, 'c');

        getServer().getPluginManager().registerEvents(new OnDeath(), this);

        Bukkit.getConsoleSender().sendMessage("§3" + configLib.lang("init").replace("%p%", "[DeathLink]"));
    }
}