package fr.toinetoine1.practice.inventory.list.teamduel;

/*
    Created by Toinetoine1 on 19/05/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.inventory.ClickAction;
import fr.toinetoine1.practice.inventory.CustomHolder;
import fr.toinetoine1.practice.inventory.Icon;
import fr.toinetoine1.practice.team.Team;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamInvKit extends CustomHolder {

    public TeamInvKit(Mode mode, Team invitedTeam) {
        super(9 * 3, "Clique sur un Kit");

        int i = 0;
        for (Kit kit : Kit.getDefaultsKit().get(mode)) {
            ItemStack itemStack = kit.getRepresentedMats().clone();
            itemStack.setAmount(1);
            ItemMeta meta = itemStack.getItemMeta().clone();
            meta.setDisplayName(kit.getFormattedName());
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemStack.setItemMeta(meta);

            setIcon(i, new Icon(itemStack).addClickAction(new ClickAction() {
                @Override
                public void execute(BadblockPlayer badblockPlayer, ItemStack clickedItem) {
                    if (invitedTeam.getOwner() == null) {
                        badblockPlayer.sendMessage(Practice.PREFIX + "Ce joueur n'est plus en ligne !");
                    } else {
                        invitedTeam.sendDuel(badblockPlayer, mode, kit);
                        badblockPlayer.closeInventory();
                        badblockPlayer.sendMessage(Practice.PREFIX+"§cDemande de Duel envoyé");
                        badblockPlayer.playSound(Sound.BURP);
                    }
                }
            }));

            i++;
        }
    }

}
