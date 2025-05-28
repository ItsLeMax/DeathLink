package de.fpm_studio.deathlink.util;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

/**
 * Adds the world generation process
 *
 * @author ItsLeMax
 * @since 1.0.0
 */
public final class WorldGeneration {

    @Getter
    private int timeUntilReset;

    @Getter
    private boolean archiveWorld;

    private World world;

    /**
     * Sets the config values
     *
     * @param timeUntilReset Time until reset of the world
     * @param archiveWorld   Should the world be archived instead of deleted?
     * @author ItsLeMax
     * @since 1.0.0
     */
    public void setConfigValues(final int timeUntilReset, final boolean archiveWorld) {
        this.timeUntilReset = timeUntilReset;
        this.archiveWorld = archiveWorld;
    }

    /**
     * Sets the world
     *
     * @param world World to rename or reset
     * @author ItsLeMax
     * @since 1.0.0
     */
    public void setWorld(@NotNull final World world) {
        this.world = world;
    }

    /**
     * Initiates the world generation process by deleting or renaming the target world, as chosen inside the config, and
     * stopping the server
     *
     * @author ItsLeMax
     * @since 1.0.0
     */
    public void initiate() {

        world.save();

        final String worldPath = Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/" + world.getName();
        final File worldFolder = new File(worldPath);

        // Rename worlds folder or delete, depending on config setting

        if (archiveWorld) {

            final String newWorldName = world.getName() + "_" + UUID.randomUUID();

            final File futureDirectory = new File(worldFolder.getParent() + "\\" + newWorldName);

            final boolean renameAttempt = worldFolder.renameTo(futureDirectory);

            if (!renameAttempt) {
                throw new RuntimeException("World renaming attempt didn't succeed, automatic processes were stopped");
            }

        } else {

            final boolean deleteAttempt = worldFolder.delete();

            if (!deleteAttempt) {
                throw new RuntimeException("World deletion attempt didn't succeed, automatic processes were stopped");
            }

        }

        // Stop the server right after to generate and load a new world

        Bukkit.getServer().shutdown();

    }

}