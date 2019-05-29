package fr.toinetoine1.practice.utils;

/*
    Created by Toinetoine1 on 27/04/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HotBarSelector {

    public static void giveSelector(BadblockPlayer player){
        player.clearInventory();
        player.getInventory().setArmorContents(null);

        ItemStack ranked = new ItemBuilder(Material.IRON_SWORD).unbreakable(true).displayname("§aUnranked").build();
        ItemStack unranked = new ItemBuilder(Material.DIAMOND_SWORD).unbreakable(true).displayname("§cRanked").build();
        ItemStack kitModification = new ItemBuilder(Material.BOOK).displayname("§9Modification de Kit").build();
        ItemStack groups = new ItemBuilder(Material.BOOKSHELF).displayname("§bListe des groupes").build();
        ItemStack statsAndParamter = new ItemBuilder(Material.PAPER).displayname("§cStats et Parametres").build();

        player.getInventory().setItem(0, ranked);
        player.getInventory().setItem(1, unranked);
        player.getInventory().setItem(2, kitModification);
        player.getInventory().setItem(5, groups);
        player.getInventory().setItem(8, statsAndParamter);
    }

}
