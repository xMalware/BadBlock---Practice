package fr.toinetoine1.practice.inventory.list;

import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.data.kit.RankedPlayerModeInfo;
import fr.toinetoine1.practice.inventory.CustomHolder;
import fr.toinetoine1.practice.inventory.Icon;
import fr.toinetoine1.practice.utils.HolographicScore;
import fr.toinetoine1.practice.utils.InfoStats;
import lombok.Getter;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Toinetoine1 on 30/05/2019.
 */

public class LeaderBoardInv extends CustomHolder {

    public LeaderBoardInv() {
        super(9, "§8Leaderboard (Kills)");

        int pos = 0;
        for (Kit kit : Kit.getDefaultsKit().get(Mode.RANKEDONE)) {
            List<String> lore = new ArrayList<>();

            List<InfoStats> get = HolographicScore.getInstance().getSortedStats().get(kit.getName());
            for (int i = 0; i < get.size(); i++) {
                InfoStats info = get.get(i);
                lore.add("§7" + (i + 1) + ": §c" + info.getPlayerName() + " §3(" + info.getInfo().getStats().get(kit.getName()).getKills() + "§3)");
            }

            ItemStack itemStack = kit.getRepresentedMats().clone();
            itemStack.setAmount(1);
            ItemMeta meta = itemStack.getItemMeta().clone();
            meta.setDisplayName(kit.getFormattedName());
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            meta.setLore(lore);
            itemStack.setItemMeta(meta);

            setIcon((pos++ + 1), new Icon(itemStack));
        }
    }

}