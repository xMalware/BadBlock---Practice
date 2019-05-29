package fr.toinetoine1.practice.events;

/*
    Created by Toinetoine1 on 12/05/2019
*/

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.inventory.list.kitmodifier.Modifier;
import fr.toinetoine1.practice.utils.ItemInventory;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener extends BadListener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        BadblockPlayer player = (BadblockPlayer) event.getPlayer();

        if (event.getView().getTopInventory().getHolder() instanceof Modifier) {
            if(InventoryClickListener.getCanCloseInv().contains(player)){
                InventoryClickListener.getCanCloseInv().remove(player);
                return;
            }

            Bukkit.getScheduler().runTaskLaterAsynchronously(Practice.getInstance(), () -> {
                player.openInventory(event.getView().getTopInventory());
                if(InventoryClickListener.getLastMoves().containsKey(player)){
                    ItemInventory itemInventory = InventoryClickListener.getLastMoves().get(player);
                    player.setItemOnCursor(itemInventory.getItemStack());
                    event.getView().getBottomInventory().remove(itemInventory.getItemStack());
                }
            }, 1);

        }
    }

}
