package org.rammex.villagefaction;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.rammex.villagefaction.commands.VillageCommand;

import java.io.File;
import java.io.IOException;

public final class VillageFaction extends JavaPlugin {
    private FileConfiguration playerconfig;


    private File PlayerDataFile;



    @Override
    public void onEnable() {

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
            this.getCommand("village").setExecutor(new VillageCommand(this));
        } else {
            /*
             * We inform about the fact that PlaceholderAPI isn't installed and then
             * disable this plugin to prevent issues.
             */
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }


        PlayerDataFile = new File(getDataFolder(), "player.yml");
        if (!PlayerDataFile.exists()) {
            PlayerDataFile.getParentFile().mkdirs();
            saveResource("player.yml", false);
        }
        playerconfig = YamlConfiguration.loadConfiguration(PlayerDataFile);





    }

    @Override
    public void onDisable() {
        try {
            playerconfig.save(PlayerDataFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public FileConfiguration getPlayerConfig(){
        return playerconfig;
    }

    public FileConfiguration getSave(){
        try {
            playerconfig.save(PlayerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




}
