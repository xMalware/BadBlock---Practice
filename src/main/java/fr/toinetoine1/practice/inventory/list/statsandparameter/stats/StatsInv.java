package fr.toinetoine1.practice.inventory.list.statsandparameter.stats;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.kit.PlayerModeInfo;
import fr.toinetoine1.practice.data.kit.RankedPlayerModeInfo;
import fr.toinetoine1.practice.inventory.ClickAction;
import fr.toinetoine1.practice.inventory.CustomHolder;
import fr.toinetoine1.practice.inventory.Icon;
import fr.toinetoine1.practice.inventory.list.statsandparameter.StatsAndParameter;
import fr.toinetoine1.practice.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Toinetoine1 on 24/05/2019.
 */

public class StatsInv extends CustomHolder {

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    static {
        numberFormat.setMinimumFractionDigits(2);
    }

    public StatsInv(PPlayer pPlayer, boolean isRanked) {
        super(9 * 3, "§eStats §3» §6" + (isRanked ? "Classé" : "Non Classé"));

        int i = 0;
        for (Mode mode : Arrays.stream(Mode.values()).filter(mode -> mode.isRanked() == isRanked).collect(Collectors.toList())) {
            PlayerModeInfo infos = pPlayer.getInfos().get(mode);
            ItemStack item = new ItemStack(Material.EMERALD);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(mode.getFormattedName());

            List<String> lore = new ArrayList<>();
            lore.add("§eTué(s): §d" + infos.getKills());
            lore.add("§eMort(s): §d" + infos.getDeaths());
            double ratio = ((double) infos.getKills() / (infos.getDeaths() == 0 ? 1 : infos.getDeaths()));
            lore.add("§6Ratio: §d" + numberFormat.format(ratio));
            if (isRanked) {
                RankedPlayerModeInfo rankedInfo = (RankedPlayerModeInfo) pPlayer.getInfos().get(mode);
                lore.add("§6ELO: §d" + rankedInfo.getPoints());
            }
            meta.setLore(lore);
            item.setItemMeta(meta);

            setIcon(i++, new Icon(item));
            setIcon(this.size - 1, new Icon(new ItemBuilder(Material.ARROW).displayname("§cRetour").build())
                    .addClickAction(new ClickAction() {
                        @Override
                        public void execute(BadblockPlayer badblockPlayer, ItemStack clickedItem) {
                            badblockPlayer.openInventory(new StatsAndParameter().getInventory());
                        }
                    }));
        }
    }

}
