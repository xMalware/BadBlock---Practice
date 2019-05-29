package fr.toinetoine1.practice.inventory;

/*
    Created by Toinetoine1 on 25/04/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import org.bukkit.inventory.ItemStack;

public interface ClickAction {

    void execute(BadblockPlayer badblockPlayer, ItemStack clickedItem);

}
