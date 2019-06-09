package fr.toinetoine1.practice.events;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.kit.ModifiedKit;
import fr.toinetoine1.practice.inventory.ClickAction;
import fr.toinetoine1.practice.inventory.CustomHolder;
import fr.toinetoine1.practice.inventory.Icon;
import fr.toinetoine1.practice.inventory.list.kitmodifier.Modifier;
import fr.toinetoine1.practice.utils.ItemInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Toinetoine1 on 23/05/2019.
 */

public class InventoryDragListener extends BadListener {

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event){
        {
            if (event.getView().getTopInventory().getHolder() instanceof CustomHolder) {

                event.setCancelled(true);

                if (event.getWhoClicked() instanceof Player) {
                    BadblockPlayer player = (BadblockPlayer) event.getWhoClicked();

                    //Get our CustomHolder
                    CustomHolder customHolder = (CustomHolder) event.getView().getTopInventory().getHolder();

                    if (customHolder instanceof Modifier) {
                        if(event.getInventorySlots().stream().anyMatch(integer -> integer >= 54)){
                            event.setCancelled(true);
                        }
                    }

                }
            }


        }
    }

}
