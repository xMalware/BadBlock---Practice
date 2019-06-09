package fr.toinetoine1.practice.inventory.list.statsandparameter;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.TimeUnit;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.inventory.ClickAction;
import fr.toinetoine1.practice.inventory.CustomHolder;
import fr.toinetoine1.practice.inventory.Icon;
import fr.toinetoine1.practice.inventory.list.statsandparameter.stats.StatsInv;
import fr.toinetoine1.practice.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Toinetoine1 on 24/05/2019.
 */


public class StatsAndParameter extends CustomHolder {

    public StatsAndParameter(BadblockPlayer player) {
        super(9 * 5, "§cStats et Parametres");
        PPlayer pPlayer = PPlayer.get(player);

        ItemStack deco = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15)).displayname("").build();
        for (int i = 18; i <= 26; i++) {
            setIcon(i, new Icon(deco));
        }

        setIcon(0, new Icon(new ItemBuilder(Material.ENCHANTED_BOOK).displayname("§3Non classé").build())
                .addClickAction(new ClickAction() {
                    @Override
                    public void execute(BadblockPlayer badblockPlayer, ItemStack clickedItem) {
                        badblockPlayer.openInventory(new StatsInv(PPlayer.get(badblockPlayer), false).getInventory());
                    }
                }));

        setIcon(1, new Icon(new ItemBuilder(Material.ENCHANTED_BOOK).displayname("§3Classé").build())
                .addClickAction(new ClickAction() {
                    @Override
                    public void execute(BadblockPlayer badblockPlayer, ItemStack clickedItem) {
                        badblockPlayer.openInventory(new StatsInv(PPlayer.get(badblockPlayer), true).getInventory());
                    }
                }));
        setIcon(2, new Icon(new ItemBuilder(Material.EMERALD).displayname("§9Temps de jeu: ").lore(new ArrayList<>(Collections.singletonList("§b" + TimeUnit.MILLIS_SECOND.toFrench((System.currentTimeMillis() - pPlayer.getCurrentTime()) + pPlayer.getCustomStats().getTimePlayed(), TimeUnit.SECOND, TimeUnit.MONTH)))).build()));
        setIcon(3, new Icon(new ItemBuilder(Material.DIAMOND_SWORD).displayname("§9Partie jouées: "+pPlayer.getCustomStats().getGamePlayed()).build()));

        setIcon(27, new Icon(new ItemBuilder(Material.BARRIER).displayname("§cAucun").build()));
    }
}
