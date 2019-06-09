package fr.toinetoine1.practice.inventory.list;

/*
    Created by Toinetoine1 on 26/04/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.core.Game;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.inventory.ClickAction;
import fr.toinetoine1.practice.inventory.CustomHolder;
import fr.toinetoine1.practice.inventory.Icon;
import fr.toinetoine1.practice.core.Queue;
import fr.toinetoine1.practice.inventory.InteractiveHolder;
import fr.toinetoine1.practice.team.Team;
import lombok.Getter;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KitChoice extends InteractiveHolder {

    @Getter
    private static Map<Mode, KitChoice> kitChoiceMap = new HashMap<>();
    @Getter
    private Map<Kit, Integer> kitSlot = new HashMap<>();

    public KitChoice(Mode mode) {
        super(9 * 2, "Clique sur un Kit");

        int i = 0;
        for (Kit kit : Kit.getDefaultsKit().get(mode)) {
            getKitSlot().put(kit, i);
            ItemStack itemStack = kit.getRepresentedMats().clone();
            itemStack.setAmount(1);
            ItemMeta meta = itemStack.getItemMeta().clone();
            meta.setDisplayName(kit.getFormattedName());
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            meta.setLore(new ArrayList<>(Arrays.asList(" ", "§cJoueurs: §9" + 0, "§cEn attente: §9"+getPlayerIsWaiting(mode, kit))));
            itemStack.setItemMeta(meta);

            setIcon(i, new Icon(itemStack).addClickAction(new ClickAction() {
                @Override
                public void execute(BadblockPlayer badblockPlayer, ItemStack clickedItem) {
                    Team team = Team.getTeam(badblockPlayer);
                    if(team != null){
                        for (BadblockPlayer player : team.getAllPlayers()){
                            if(Game.isInGame(player))
                                return;
                        }
                    }
                    Queue.addPlayerInQueue(badblockPlayer, mode, kit);
                    badblockPlayer.closeInventory();
                }
            }));

            i++;
        }
    }

    public void addIGPlayer(Mode mode, Kit kit, int size){
        Icon icon = getIcon(getKitSlot().get(kit));
        icon.addAmount(size * 2 + (icon.getAmount() == 1 ? -1 : 0));
        icon.setLore(new ArrayList<>(Arrays.asList(" ", "§cJoueurs: §9" + getPlayersInMode(mode, kit), "§cEn attente: §9"+getPlayerIsWaiting(mode, kit))));
        setIcon(getKitSlot().get(kit), icon);
    }

    public void removeIGPlayer(Mode mode, Kit kit, int size) {
        Icon icon = getIcon(getKitSlot().get(kit));
        icon.removeAmount(size * 2 + (icon.getAmount() == 2 ? -1 : 0));
        icon.setLore(new ArrayList<>(Arrays.asList(" ", "§cJoueurs: §9" + getPlayersInMode(mode, kit), "§cEn attente: §9"+getPlayerIsWaiting(mode, kit))));
        setIcon(getKitSlot().get(kit), icon);
    }

    public void updateQueue(Mode mode, Kit kit){
        Icon icon = getIcon(getKitSlot().get(kit));
        icon.setLore(new ArrayList<>(Arrays.asList(" ", "§cJoueurs: §9" + getPlayersInMode(mode, kit), "§cEn attente: §9"+getPlayerIsWaiting(mode, kit))));
        setIcon(getKitSlot().get(kit), icon);
    }

    private static int getPlayersInMode(Mode mode, Kit kit) {
        return Practice.getGames().stream().filter(game -> game.getMode() == mode).filter(game -> game.getKit() == kit).mapToInt(value -> value.getUnions().size()).sum();
    }

    private static int getPlayerIsWaiting(Mode mode, Kit kit){
        return Queue.getWaitingQueue().get(mode).getInQueue().get(kit).size();
    }
}
