package fr.toinetoine1.practice.inventory.list.kitmodifier;

/*
    Created by Toinetoine1 on 11/05/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.core.Queue;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.inventory.ClickAction;
import fr.toinetoine1.practice.inventory.CustomHolder;
import fr.toinetoine1.practice.inventory.Icon;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;


public class KitModifier extends CustomHolder {

    public KitModifier(Mode mode) {
        super(9 * 3, "§9Modifieur de Kit");

        int i = 0;
        for (Kit kit : Kit.getDefaultsKit().get(mode)) {
            ItemStack itemStack = kit.getRepresentedMats().clone();
            itemStack.setAmount(1);
            ItemMeta meta = itemStack.getItemMeta().clone();
            meta.setDisplayName(kit.getFormattedName());
            meta.setLore(new ArrayList<>());
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemStack.setItemMeta(meta);

            setIcon(i, new Icon(itemStack).addClickAction(new ClickAction() {
                @Override
                public void execute(BadblockPlayer badblockPlayer, ItemStack clickedItem) {
                    badblockPlayer.openInventory(new Modifier(badblockPlayer, mode, kit).getInventory());
                }
            }));

            i++;
        }
    }
}
