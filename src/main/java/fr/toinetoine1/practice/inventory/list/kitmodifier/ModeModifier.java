package fr.toinetoine1.practice.inventory.list.kitmodifier;

/*
    Created by Toinetoine1 on 11/05/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.inventory.ClickAction;
import fr.toinetoine1.practice.inventory.CustomHolder;
import fr.toinetoine1.practice.inventory.Icon;
import fr.toinetoine1.practice.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ModeModifier extends CustomHolder {

    public ModeModifier() {
        super(9 * 3, "§9Modifieur de Kit");

        int i = 0;
        for(Mode mode : Mode.values()){
            setIcon(i++, new Icon(new ItemBuilder(Material.ENCHANTED_BOOK).displayname(mode.getFormattedName()).build()).addClickAction(new ClickAction() {
                @Override
                public void execute(BadblockPlayer badblockPlayer, ItemStack clickedItem) {
                    badblockPlayer.openInventory(new KitModifier(mode).getInventory());
                }
            }));
        }
    }
}