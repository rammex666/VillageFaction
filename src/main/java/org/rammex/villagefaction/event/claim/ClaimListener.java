package org.rammex.villagefaction.event.claim;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.rammex.villagefaction.GamePlayer;
import org.rammex.villagefaction.VillageFaction;

public class ClaimListener implements Listener {

    VillageFaction plugin;

    public ClaimListener(VillageFaction plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event){
        new GamePlayer(event.getPlayer().getName());
    }
}
