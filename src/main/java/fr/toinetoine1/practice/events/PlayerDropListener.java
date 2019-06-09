package fr.toinetoine1.practice.events;

/*
    Created by Toinetoine1 on 13/05/2019
*/

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropListener extends BadListener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrop(PlayerDropItemEvent event){
        BadblockPlayer player = (BadblockPlayer) event.getPlayer();

        if(InventoryClickListener.getLastMoves().containsKey(player)){
            event.getItemDrop().remove();
            event.setCancelled(true);
        }

    }

}
