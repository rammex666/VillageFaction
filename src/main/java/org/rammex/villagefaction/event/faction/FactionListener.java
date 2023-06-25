package org.rammex.villagefaction.event.faction;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.FactionDisbandEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.rammex.villagefaction.VillageFaction;

public class FactionListener implements Listener {

    VillageFaction plugin;
    public FactionListener(VillageFaction plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFactionMemberLeave(FPlayerLeaveEvent event) {
        FPlayer player = event.getfPlayer();
        this.plugin.getPlayerConfig().set("players." + player.getName() + ".village", false);
        this.plugin.getSave();
    }

    @EventHandler
    public void onFactionDisband(FactionDisbandEvent event) {
        Faction faction = event.getFaction();
        for (FPlayer fplayer : faction.getFPlayers()) {
            this.plugin.getPlayerConfig().set("players." + fplayer.getName() + ".village", false);
            this.plugin.getSave();
        }

    }
}
