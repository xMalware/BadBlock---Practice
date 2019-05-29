package fr.toinetoine1.practice.inventory.list;

/*
    Created by Toinetoine1 on 26/04/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.inventory.ClickAction;
import fr.toinetoine1.practice.inventory.CustomHolder;
import fr.toinetoine1.practice.inventory.Icon;
import fr.toinetoine1.practice.core.Queue;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class KitChoice extends CustomHolder {

    public KitChoice(Mode mode) {
        super(9 * 3, "Clique sur un Kit");

        int i = 0;
        for (Kit kit : Kit.getDefaultsKit().get(mode)) {
            ItemStack itemStack = kit.getRepresentedMats().clone();
            ItemMeta meta = itemStack.getItemMeta().clone();
            meta.setDisplayName(kit.getFormattedName());
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            meta.setLore(new ArrayList<>(Arrays.asList(" ", "§cJoueurs: §9" + getPlayersInMode(mode, kit), "§cEn attente: §9"+getPlayerIsWaiting(mode, kit))));
            itemStack.setItemMeta(meta);

            setIcon(i, new Icon(itemStack).addClickAction(new ClickAction() {
                @Override
                public void execute(BadblockPlayer badblockPlayer, ItemStack clickedItem) {
                    Queue.addPlayerInQueue(badblockPlayer, mode, kit);
                    badblockPlayer.closeInventory();
                }
            }));

            i++;
        }
    }

    private static int getPlayersInMode(Mode mode, Kit kit) {
        System.out.println(Practice.getGames().stream().filter(game -> game.getMode() == mode).filter(game -> game.getKit() == kit).mapToInt(value -> value.getUnions().size()).sum());
        return Practice.getGames().stream().filter(game -> game.getMode() == mode).filter(game -> game.getKit() == kit).mapToInt(value -> value.getUnions().size()).sum();
    }

    private static int getPlayerIsWaiting(Mode mode, Kit kit){
        return Queue.getWaitingQueue().get(mode).getInQueue().get(kit).size();
    }


}
