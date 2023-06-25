package org.rammex.villagefaction.commands;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.rammex.villagefaction.VillageFaction;

public class VillageCommand implements CommandExecutor {

    VillageFaction plugin;

    public VillageCommand(VillageFaction plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            if(hasVillage(player)){
                VillageMainMenu(player);
            } else{
                ErrorMessage(player);
            }
        } else {
            if (args[0].equalsIgnoreCase("create")) {
                if (isPlayerFactionLeader(player)) {
                    if (hasVillage(player)) {
                        player.sendMessage(ChatColor.RED + "[Error] Vous ne pouvez avoir qu'un seul village de faction !");
                    } else {
                        VillageCreateMenu(player);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "[Error] Vous ne pouvez créer de village de faction que si vous avez le rôle de Leader !");
                }
            }
            if (args[0].equalsIgnoreCase("help")){
                ErrorMessage(player);
            }
        }
        return false;
    }

    private boolean hasVillage(Player player) {
        return this.plugin.getPlayerConfig().getBoolean("players." + player.getName() + ".village");
    }

    public boolean isPlayerFactionLeader(Player player) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Role role = fPlayer.getRole();
        if (role == Role.LEADER){
            return true;
        } else{
            return false;
        }
    }

    public void VillageCreateMenu(Player player){
        Inventory inv = Bukkit.createInventory(null, 27, "§a§l✯ Création de Village ✯");

        inv.setItem(11, getItem(Material.BOOK, "§eInformation"));
        inv.setItem(13, getItem(Material.GREEN_CONCRETE, "§eCréation"));
        inv.setItem(15, getItem(Material.PLAYER_HEAD, "§eClassement"));
        player.openInventory(inv);
    }

    public void VillageMainMenu(Player player){
        Inventory inv = Bukkit.createInventory(null, 9*6, "§0§l✯ Menu Principale Village ✯");

        inv.setItem(0, getItem(Material.BLACK_STAINED_GLASS_PANE, "§0"));
        inv.setItem(1, getItem(Material.BLACK_STAINED_GLASS_PANE, "§0"));
        inv.setItem(9, getItem(Material.BLACK_STAINED_GLASS_PANE, "§0"));
        inv.setItem(8, getItem(Material.BLACK_STAINED_GLASS_PANE, "§0"));
        inv.setItem(7, getItem(Material.BLACK_STAINED_GLASS_PANE, "§0"));
        inv.setItem(17, getItem(Material.BLACK_STAINED_GLASS_PANE, "§0"));
        inv.setItem(46, getItem(Material.BLACK_STAINED_GLASS_PANE, "§0"));
        inv.setItem(45, getItem(Material.BLACK_STAINED_GLASS_PANE, "§0"));
        inv.setItem(36, getItem(Material.BLACK_STAINED_GLASS_PANE, "§0"));
        inv.setItem(52, getItem(Material.BLACK_STAINED_GLASS_PANE, "§0"));
        inv.setItem(53, getItem(Material.BLACK_STAINED_GLASS_PANE, "§0"));
        inv.setItem(45, getItem(Material.BLACK_STAINED_GLASS_PANE, "§0"));

        inv.setItem(12, getItem(Material.PAPER, "§eStatistiques"));
        inv.setItem(13, getItem(Material.PLAYER_HEAD, "§eMembres"));
        inv.setItem(14, getItem(Material.BEACON, "§eClassement"));
        inv.setItem(20, getItem(Material.COMPASS, "§eHome"));
        inv.setItem(21, getItem(Material.DIAMOND, "§eUpgrades"));
        inv.setItem(22, getItem(Material.REDSTONE, "§eSettings"));
        inv.setItem(23, getItem(Material.GOLD_INGOT, "§eBanque"));
        inv.setItem(24, getItem(Material.BEDROCK, "§e"));

        player.openInventory(inv);
    }

    public void ErrorMessage(Player player){
        player.sendMessage(ChatColor.LIGHT_PURPLE+ "------Village De Faction------");
        player.sendMessage(ChatColor.YELLOW+ "/village help ( obtient les commandes du plugin )");
        player.sendMessage(ChatColor.YELLOW+ "/village create ( permet de crée un village de faction )");
        player.sendMessage(ChatColor.LIGHT_PURPLE+ "------------------------------");
        player.sendMessage(ChatColor.LIGHT_PURPLE+ "--------By Rammex--------");
        player.sendMessage(ChatColor.LIGHT_PURPLE+ "------------------------------");
    }

    public ItemStack getItem(Material material, String customName) {
        ItemStack it = new ItemStack(material);
        ItemMeta customM = it.getItemMeta();
        if (customName != null) customM.setDisplayName(customName);
        it.setItemMeta(customM);
        return it;
    }

    private void saveConfig() {
        this.plugin.getSave();
    }
}
