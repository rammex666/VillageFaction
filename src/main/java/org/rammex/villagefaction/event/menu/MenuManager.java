package org.rammex.villagefaction.event.menu;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.rammex.villagefaction.GamePlayer;
import org.rammex.villagefaction.VillageFaction;
import org.rammex.villagefaction.egionManager;

import java.io.IOException;
import java.util.Set;

public class MenuManager implements Listener {
    VillageFaction plugin;
    public MenuManager(VillageFaction plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory inv = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if (current == null) return;

        if (event.getView().getTitle().equalsIgnoreCase("§a§l✯ Création de Village ✯")) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);


            //Information
            if(current.getType() == Material.BOOK){
                player.closeInventory();
            }
            //Création
            if(current.getType() == Material.GREEN_CONCRETE){
                if(getFaction(player)){
                    if(getNombreMembresFaction(player) >= 1){
                        player.closeInventory();
                        VillageCreateConfirmMenu(player);
                    } else {
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED+"[Error] Vous devez avoir 4 membre ou plus !");
                    }
                } else{
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED+"[Error] Vous n'avez pas de faction !");
                }

            }
            //Classement
            if(current.getType() == Material.PLAYER_HEAD){
                player.closeInventory();
            }
        }

        if (event.getView().getTitle().equalsIgnoreCase("§a§l✯ Confirmation Création Village ✯")){
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

            if(current.getType() == Material.GREEN_CONCRETE){
                this.plugin.getPlayerConfig().set("players." + player.getName() + ".village", true);
                this.plugin.getSave();
                creerZoneFaction(player);
                player.closeInventory();
            }
            if(current.getType() == Material.RED_CONCRETE){
                player.closeInventory();
            }
            if(current.getType() == Material.BARRIER){
                player.closeInventory();
                VillageCreateMenu(player);

            }


        }
    }



    public boolean getFaction(Player player) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        if(fPlayer.hasFaction()){
            return true;
        } else{
            return false;
        }

    }



    public int getNombreMembresFaction(Player player) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player); // Remplacez "Factions" par le plugin Factions que vous utilisez
        Faction faction = fPlayer.getFaction();


        if (faction == null) {
            return 0;
        }

        int nombreMembres = 0;

        for (FPlayer fplayer : faction.getFPlayers()) {
            nombreMembres++;
        }

        return nombreMembres;
    }


    public void VillageCreateConfirmMenu(Player player){
        Inventory inv = Bukkit.createInventory(null, 27, "§a§l✯ Confirmation Création Village ✯");

        inv.setItem(12, getItem(Material.GREEN_CONCRETE, "§aCréation"));
        inv.setItem(14, getItem(Material.RED_CONCRETE, "§4Annulation"));
        inv.setItem(18, getItem(Material.BARRIER, "§cRetour"));
        player.openInventory(inv);
    }


    public ItemStack getItem(Material material, String customName) {
        ItemStack it = new ItemStack(material);
        ItemMeta customM = it.getItemMeta();
        if (customName != null) customM.setDisplayName(customName);
        it.setItemMeta(customM);
        return it;
    }

    public void VillageCreateMenu(Player player){
        Inventory inv = Bukkit.createInventory(null, 27, "§a§l✯ Création de Village ✯");

        inv.setItem(11, getItem(Material.BOOK, "§eInformation"));
        inv.setItem(13, getItem(Material.GREEN_CONCRETE, "§eCréation"));
        inv.setItem(15, getItem(Material.PLAYER_HEAD, "§eClassement"));
        player.openInventory(inv);
    }

    public boolean creerZoneFaction(Player player) {
        GamePlayer gp = GamePlayer.gamePlayers.get(player.getName());

        Location playerLocation = player.getLocation();
        World world = playerLocation.getWorld();
        double playerX = playerLocation.getX();
        double playerY = playerLocation.getY();
        double playerZ = playerLocation.getZ();

        double minX = playerX - 15;
        double minZ = playerZ - 15;
        double maxX = playerX + 15;
        double maxZ = playerZ + 15;

        Location minLocation = new Location(world, minX, playerY, minZ);
        Location maxLocation = new Location(world, maxX, playerY, maxZ);

        // Vérifier si la zone est déjà réclamée par une autre faction ou joueur
        for (egionManager regionGet : this.plugin.regions) {
            if (regionGet.isInArea(minLocation) || regionGet.isInArea(maxLocation) || regionGet.isInArea(playerLocation)) {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas créer votre village ici !");
                return false;
            }
        }

        // Créer la zone de faction
        String[] loc = new String[]{minLocation.getBlockX() + "" + minLocation.getBlockY() + "" + minLocation.getBlockZ() + "" + maxLocation.getBlockX() + "" + maxLocation.getBlockY() + "" + maxLocation.getBlockZ(), world.getName(), player.getName()};
        this.plugin.getPlayerConfig().set("players." + player.getName() + ".zone", loc);
        this.plugin.getSave();

        // Ajouter la région à la liste des régions
        egionManager region = new egionManager(minLocation, maxLocation, player.getName());
        this.plugin.regions.add(region);

        player.sendMessage(ChatColor.GREEN + "Vous avez créé votre village de faction avec succès !");
        return true;
    }


    private Faction getFactionObject(Player player) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        return fPlayer.getFaction();
    }






}
