package org.rammex.villagefaction;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.rammex.villagefaction.commands.VillageCommand;
import org.rammex.villagefaction.event.claim.ClaimListener;
import org.rammex.villagefaction.event.faction.FactionListener;
import org.rammex.villagefaction.event.menu.MenuManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class VillageFaction extends JavaPlugin {
    private FileConfiguration playerConfig;
    private File playerDataFile;
    public List<egionManager> regions;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.getCommand("village").setExecutor(new VillageCommand(this));
            getServer().getPluginManager().registerEvents(new MenuManager(this), this);
            getServer().getPluginManager().registerEvents(new FactionListener(this), this);
            getServer().getPluginManager().registerEvents(new ClaimListener(this), this);
            regions = new ArrayList<>();
        } else {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        playerDataFile = new File(getDataFolder(), "player.yml");
        if (!playerDataFile.exists()) {
            playerDataFile.getParentFile().mkdirs();
            saveResource("player.yml", false);
        }
        playerConfig = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    @Override
    public void onDisable() {
        try {
            playerConfig.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getPlayerConfig() {
        return playerConfig;
    }

    public void getSave() {
        try {
            playerConfig.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
