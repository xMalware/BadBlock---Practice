package fr.toinetoine1.practice.inventory.list.kitmodifier;

/*
    Created by Toinetoine1 on 11/05/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.inventory.CustomHolder;
import fr.toinetoine1.practice.inventory.Icon;
import fr.toinetoine1.practice.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Modifier extends CustomHolder {

    public Modifier(BadblockPlayer player, Mode mode, Kit kit) {
        super(9 * 6, mode.getFormattedName() + " §3» " + kit.getFormattedName());

        PPlayer pPlayer = PPlayer.get(player);
        System.out.println(pPlayer);
        System.out.println(pPlayer.getInfos());
        System.out.println(pPlayer.getInfos().get(mode));
        ItemStack[] contents;
        if (pPlayer.getInfos().get(mode).getModified() != null && pPlayer.getInfos().get(mode).getModified().containsKey(kit.getFormattedName())){
            contents = pPlayer.getInfos().get(mode).getModified().get(kit.getFormattedName()).getContents();
        } else {
            if(pPlayer.getInfos().get(mode).getModified() == null)
                pPlayer.getInfos().get(mode).setModified(new HashMap<>());
            contents = kit.getContents();
        }

        for (int i = 9; i < contents.length; i++) {
            setIcon(i - 9, new Icon(contents[i]));
        }
        for (int i = 0; i < 9; i++) {
            setIcon(i + 9 * 4, new Icon(contents[i]));
        }
        for (int i = 27; i < this.size; i++) {
            if (between(i, 27, 35)) {
                setIcon(i, new Icon(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15)).displayname("§7Votre inventaire: §c⬆").lore(new ArrayList<>(Arrays.asList("§7Votre HotBar: §c⬇"))).build()));
            } else if (between(i, 45, this.size - 1)) {
                setIcon(i, new Icon(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15)).displayname("§7Votre HotBar: §c⬆").build()));
            }
            setIcon(this.size - 1, new Icon(new ItemBuilder(Material.EMERALD).displayname("§aValider").build()));
            setIcon(this.size - 2, new Icon(new ItemBuilder(Material.REDSTONE).displayname("§cAnnuler").build()));
        }
    }

    private boolean between(int i, int min, int max) {
        return (i >= min && i <= max);
    }

}
